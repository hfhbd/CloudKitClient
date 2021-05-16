package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.Record.*
import app.softwork.cloudkitclient.internal.*
import app.softwork.cloudkitclient.types.*
import app.softwork.cloudkitclient.values.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.datetime.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlin.reflect.*

public class CKClient(
    public val container: String,
    keyID: String,
    private val privateECPrime256v1Key: ByteArray,
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
        }.toRegularResponse(recordInformation)

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
            }.toRegularResponse(recordInformation).first()

        override suspend fun <F : Fields, R : Record<F>> read(
            recordName: String,
            recordInformation: Information<F, R>,
            zoneID: ZoneID
        ): R? = request("/records/lookup", Request.RecordLookup.serializer()) {
            Request.RecordLookup(listOf(Request.RecordLookup.RecordName(recordName = recordName)), zoneID = zoneID)
        }.handleError(recordInformation).run {
            when (this) {
                is Holder.Success -> response.records.single()
                is Holder.Failure -> {
                    if (error.serverErrorCode == "NOT_FOUND") null else throw error
                }
            }
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
            }.toRegularResponse(recordInformation).first()

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
                json.decodeFromString(Asset.Upload.Response.serializer(), it)
            }.tokens.first().let {
                httpClient { }.post<String>(it.url) { body = asset }
            }.let {
                json.decodeFromString(Asset.Upload.Response.SingleFile.serializer(), it)
            }.singleFile

        override suspend fun createToken(): Push.Response = request("/tokens/create", Push.Create.serializer()) {
            Push.Create(environment)
        }.let {
            json.decodeFromString(Push.Response.serializer(), it)
        }
    }

    private suspend fun <In> Database.request(
        resource: String,
        serializer: KSerializer<In>,
        requestBody: () -> In
    ): String = client.request {
        method = HttpMethod.Post

        val subPath = "/database/1/$container/$environment/$name$resource"

        val date = Clock.System.now().toString().dropLastWhile { it != '.' }.replace('.', 'Z')
        val body = json.encodeToString(serializer, requestBody())

        val bodySignature = sha256(body).encodeBase64
        val signature = ecdsa(privateECPrime256v1Key, "$date:$bodySignature:$subPath")

        url.takeFrom(subPath)
        header("X-Apple-CloudKit-Request-ISO8601Date", date)
        header("X-Apple-CloudKit-Request-SignatureV1", signature)
        this.body = body
    }

    private fun <F : Fields, R : Record<F>> String.toRegularResponse(recordInformation: Information<F, R>): List<R> {
        val response = json.decodeFromString(
            Response.serializer(
                recordInformation.fieldsSerializer(),
                recordInformation.serializer()
            ), this
        )
        return response.records
    }


    private fun <F : Fields, R : Record<F>> String.handleError(
        recordInformation: Information<F, R>
    ): Holder<F, R> =
        try {
            val response = json.decodeFromString(
                Response.serializer(
                    recordInformation.fieldsSerializer(),
                    recordInformation.serializer()
                ), this
            )
            Holder.Success(response)
        } catch (e: SerializationException) {
            try {
                val error = json.decodeFromString(Holder.Failure.ReadError.serializer(), this)
                Holder.Failure(error.records.first())
            } catch (error: SerializationException) {
                throw error
            }
        }

    private sealed class Holder<F : Fields, R : Record<F>> {
        class Success<F : Fields, R : Record<F>>(val response: Response<F, R>) : Holder<F, R>()
        class Failure<F : Fields, R : Record<F>>(val error: Error) : Holder<F, R>() {
            @Serializable
            data class ReadError(val records: List<Error>)
        }
    }
}
