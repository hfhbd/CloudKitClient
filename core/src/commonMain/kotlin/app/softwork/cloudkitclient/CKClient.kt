package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.Lookup.Response.*
import app.softwork.cloudkitclient.Record.*
import app.softwork.cloudkitclient.internal.*
import app.softwork.cloudkitclient.types.*
import app.softwork.cloudkitclient.types.Asset.Upload.*
import app.softwork.cloudkitclient.types.Asset.Upload.Response.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.datetime.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

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

    public inner class Database internal constructor(internal val name: String) : Client.Database {
        // temp fix
        public override suspend fun lookup(
            lookupRecord: Lookup.Request
        ): List<SomeUser> =
            request("/users/lookup/email", Lookup.Request.serializer()) {
                lookupRecord
            }.let {
                json.decodeFromString(Lookup.Response.serializer(), it)
            }.users

        public override suspend fun <F : Fields, R : Record<F>> query(
            recordInformation: Record.Information<F, R>,
            zoneID: ZoneID,
            filter: Filter.Builder<F>.() -> Unit,
            sort: Sort.Builder<F>.() -> Unit
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
            recordInformation: Record.Information<F, R>
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

        public override suspend fun <F : Fields, R : Record<F>> update(
            record: R,
            recordInformation: Record.Information<F, R>
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
            recordInformation: Record.Information<F, R>
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

        public override suspend fun upload(asset: ByteArray, field: Field, zoneID: ZoneID): List<Token> =
            request("/asset/upload", Asset.Upload.serializer()) {
                Asset.Upload(tokens = listOf(field), zoneID = zoneID)
            }.let {
                json.decodeFromString(Asset.Upload.Response.serializer(), it)
            }.tokens.onEach {
                client.post(it.url) { body = asset }
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

    private fun <F : Fields, R : Record<F>> String.toRegularResponse(recordInformation: Record.Information<F, R>) =
        let {
            println(it)
            json.decodeFromString(
                Response.serializer(
                    recordInformation.fieldsSerializer(),
                    recordInformation.serializer()
                ), it
            )
        }.records
}
