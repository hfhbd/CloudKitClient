package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.Record.Information
import app.softwork.cloudkitclient.internal.ecdsa
import app.softwork.cloudkitclient.internal.httpClient
import app.softwork.cloudkitclient.internal.sha256
import app.softwork.cloudkitclient.types.Asset
import app.softwork.cloudkitclient.values.Value
import io.ktor.client.call.body
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.http.takeFrom
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.reflect.KProperty1
import kotlin.time.Clock

public class CKClient(
    public val container: String,
    keyID: String,
    private val privateECPrime256v1Key: ByteArray,
    override val logging: (String) -> Unit = { },
    public val environment: Environment = Environment.Development,
    private val clock: Clock = Clock.System,
) : Client {

    public companion object {
        private val json = Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
            useAlternativeNames = false
        }
    }

    private val client = httpClient {
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "api.apple-cloudkit.com"
            }
            header("X-Apple-CloudKit-Request-KeyID", keyID)
        }
    }

    public override val publicDB: Database = Database("public")
    public override val privateDB: Database = Database("private")
    public override val sharedDB: Database = Database("shared")

    override suspend fun download(assetToDownload: Asset): ByteArray =
        client.get(assetToDownload.downloadURL!!).body()

    public inner class Database internal constructor(internal val name: String) : Client.Database {
        public override suspend fun <Fields, R : Record<Fields>> query(
            recordInformation: Information<Fields, R>,
            zoneID: ZoneID,
            sort: Sort.Builder<Fields>.() -> Unit,
            filter: Filter.Builder<Fields>.() -> Unit
        ): List<R> = request("/records/query", Request.serializer()) {
            Request(
                zoneID = zoneID,
                query = Query(
                    recordType = recordInformation.recordType,
                    filterBy = Filter.Builder<Fields>().apply(filter).build(),
                    sortBy = Sort.Builder<Fields>().apply(sort).build()
                )
            )
        }.toResponse(recordInformation)

        public override suspend fun <Fields, R : Record<Fields>> create(
            record: R,
            recordInformation: Information<Fields, R>
        ): R =
            request(
                "/records/modify",
                OperationsRequest.serializer(
                    recordInformation.fieldsSerializer(),
                    recordInformation.serializer(),
                    Create.serializer(recordInformation.fieldsSerializer(), recordInformation.serializer())
                )
            ) {
                OperationsRequest(operations = listOf(Create(record = record)))
            }.toResponse(recordInformation).single()

        override suspend fun <Fields, R : Record<Fields>> read(
            recordName: String,
            recordInformation: Information<Fields, R>,
            zoneID: ZoneID
        ): R? = try {
            request("/records/lookup", Request.RecordLookup.serializer()) {
                Request.RecordLookup(
                    listOf(Request.RecordLookup.RecordName(recordName = recordName)),
                    zoneID = zoneID
                )
            }.toResponse(recordInformation).single()
        } catch (error: CKError) {
            if (error.serverErrorCode == "NOT_FOUND") null else throw error
        }

        public override suspend fun <Fields, R : Record<Fields>> update(
            record: R,
            recordInformation: Information<Fields, R>
        ): R =
            request(
                "/records/modify",
                OperationsRequest.serializer(
                    recordInformation.fieldsSerializer(),
                    recordInformation.serializer(),
                    Update.serializer(recordInformation.fieldsSerializer(), recordInformation.serializer())
                )
            ) {
                OperationsRequest(operations = listOf(Update(record = record)))
            }.toResponse(recordInformation).single()

        public override suspend fun <Fields, R : Record<Fields>> delete(
            record: R,
            recordInformation: Information<Fields, R>
        ) {
            request(
                "/records/modify",
                OperationsRequest.serializer(
                    recordInformation.fieldsSerializer(),
                    recordInformation.serializer(),
                    Delete.serializer(recordInformation.fieldsSerializer(), recordInformation.serializer())
                )
            ) {
                OperationsRequest(operations = listOf(Delete(record = record)))
            }
        }

        override suspend fun <Fields, R : Record<Fields>> upload(
            asset: ByteArray,
            recordInformation: Information<Fields, R>,
            field: KProperty1<Fields, Value.Asset?>,
            recordName: String?,
            zoneID: ZoneID
        ): Asset = request("/assets/upload", Asset.Upload.serializer()) {
            Asset.Upload(
                tokens = listOf(
                    Asset.Upload.Field(
                        fieldName = field.name,
                        recordInformation.recordType,
                        recordName = recordName
                    )
                ),
                zoneID = zoneID
            )
        }.let {
            json.decodeFromString(Asset.Upload.Response.serializer(), it.bodyAsText())
        }.tokens.first().let {
            client.post(it.url) { setBody(asset) }.bodyAsText()
        }.let {
            json.decodeFromString(Asset.Upload.Response.SingleFile.serializer(), it)
        }.singleFile

        override suspend fun createToken(): Push.Response = request("/tokens/create", Push.Create.serializer()) {
            Push.Create(environment)
        }.let {
            json.decodeFromString(Push.Response.serializer(), it.bodyAsText())
        }
    }

    private suspend fun <In> Database.request(
        resource: String,
        serializer: KSerializer<In>,
        requestBody: () -> In
    ): HttpResponse = client.request {
        method = HttpMethod.Post

        val subPath = "/database/1/$container/$environment/$name$resource"

        val date = clock.now().toString().dropLastWhile { it != '.' }.replace('.', 'Z')
        val body = json.encodeToString(serializer, requestBody())
        logging("request: $body")
        @OptIn(ExperimentalEncodingApi::class)
        val bodySignature = Base64.encode(sha256(body))
        val signature = ecdsa(privateECPrime256v1Key, "$date:$bodySignature:$subPath")

        url.takeFrom(subPath)
        header("X-Apple-CloudKit-Request-ISO8601Date", date)
        header("X-Apple-CloudKit-Request-SignatureV1", signature)
        setBody(body)
    }

    private suspend fun <Fields, R : Record<Fields>> HttpResponse.toResponse(
        recordInformation: Information<Fields, R>
    ): List<R> {
        val body = bodyAsText()
        logging("response ($status): $body")
        try {
            return json.decodeFromString(
                Response.serializer(
                    recordInformation.fieldsSerializer(),
                    recordInformation.serializer()
                ),
                body
            ).records
        } catch (error: SerializationException) {
            throw json.decodeFromString(ResponseError.serializer(), body).records.firstOrNull() ?: throw error
        }
    }

    @Serializable
    private data class ResponseError(val records: List<CKError>)
}
