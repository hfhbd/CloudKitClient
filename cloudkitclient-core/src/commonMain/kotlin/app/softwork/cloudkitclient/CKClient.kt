package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.Record.*
import app.softwork.cloudkitclient.internal.*
import app.softwork.cloudkitclient.types.*
import app.softwork.cloudkitclient.values.*
import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.datetime.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlin.reflect.*

public class CKClient(
    public val container: String,
    keyID: String,
    private val privateECPrime256v1Key: ByteArray,
    override val logging: (String) -> Unit = { },
    public val environment: Environment = Environment.Development
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

    override suspend fun download(assetToDownload: Asset): ByteArray = httpClient { }.get(assetToDownload.downloadURL!!)

    public inner class Database internal constructor(internal val name: String) : Client.Database {
        public override suspend fun <F : Fields, R : Record<F>> query(
            recordInformation: Information<F, R>,
            zoneID: ZoneID,
            sort: Sort.Builder<F>.() -> Unit,
            filter: Filter.Builder<F>.() -> Unit
        ): List<R> = request("/records/query", Request.serializer()) {
            Request(
                zoneID = zoneID,
                query = Query(
                    recordType = recordInformation.recordType,
                    filterBy = Filter.Builder<F>().apply(filter).build(),
                    sortBy = Sort.Builder<F>().apply(sort).build()
                )
            )
        }.toResponse(recordInformation)

        public override suspend fun <F : Fields, R : Record<F>> create(
            record: R,
            recordInformation: Information<F, R>
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


        override suspend fun <F : Fields, R : Record<F>> read(
            recordName: String,
            recordInformation: Information<F, R>,
            zoneID: ZoneID
        ): R? = try {
            request("/records/lookup", Request.RecordLookup.serializer()) {
                Request.RecordLookup(
                    listOf(Request.RecordLookup.RecordName(recordName = recordName)),
                    zoneID = zoneID
                )
            }.toResponse(recordInformation).single()
        } catch (error: Error) {
            if (error.serverErrorCode == "NOT_FOUND") null else throw error
        }


        public override suspend fun <F : Fields, R : Record<F>> update(
            record: R,
            recordInformation: Information<F, R>
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

        public override suspend fun <F : Fields, R : Record<F>> delete(
            record: R,
            recordInformation: Information<F, R>
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

        override suspend fun <F : Fields, R : Record<F>> upload(
            asset: ByteArray,
            recordInformation: Information<F, R>,
            field: KProperty1<F, Value.Asset?>,
            recordName: String?,
            zoneID: ZoneID
        ): Asset =
            request("/assets/upload", Asset.Upload.serializer()) {
                Asset.Upload(
                    tokens = listOf(
                        Asset.Upload.Field(
                            fieldName = field.name,
                            recordInformation.recordType,
                            recordName = recordName
                        )
                    ), zoneID = zoneID
                )
            }.let {
                json.decodeFromString(Asset.Upload.Response.serializer(), it.receive())
            }.tokens.first().let {
                httpClient { }.post<String>(it.url) { body = asset }
            }.let {
                json.decodeFromString(Asset.Upload.Response.SingleFile.serializer(), it)
            }.singleFile

        override suspend fun createToken(): Push.Response = request("/tokens/create", Push.Create.serializer()) {
            Push.Create(environment)
        }.let {
            json.decodeFromString(Push.Response.serializer(), it.receive())
        }
    }

    private suspend fun <In> Database.request(
        resource: String,
        serializer: KSerializer<In>,
        requestBody: () -> In
    ): HttpResponse = client.request {
        method = HttpMethod.Post

        val subPath = "/database/1/$container/$environment/$name$resource"

        val date = Clock.System.now().toString().dropLastWhile { it != '.' }.replace('.', 'Z')
        val body = json.encodeToString(serializer, requestBody())
        logging("request: $body")
        val bodySignature = sha256(body).encodeBase64
        val signature = ecdsa(privateECPrime256v1Key, "$date:$bodySignature:$subPath")

        url.takeFrom(subPath)
        header("X-Apple-CloudKit-Request-ISO8601Date", date)
        header("X-Apple-CloudKit-Request-SignatureV1", signature)
        this.body = body
    }


    private suspend fun <F : Fields, R : Record<F>> HttpResponse.toResponse(
        recordInformation: Information<F, R>
    ): List<R> {
        val body = receive<String>()
        logging("response ($status): $body")
        try {
            return json.decodeFromString(
                Response.serializer(
                    recordInformation.fieldsSerializer(),
                    recordInformation.serializer()
                ), body
            ).records

        } catch (error: SerializationException) {
            throw json.decodeFromString(ResponseError.serializer(), body).records.firstOrNull() ?: throw error
        }
    }

    @Serializable
    private data class ResponseError(val records: List<Error>)
}
