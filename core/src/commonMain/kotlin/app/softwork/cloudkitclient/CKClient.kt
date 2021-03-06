package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.Lookup.Response.*
import app.softwork.cloudkitclient.internal.*
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
) {

    public companion object {
        private val json = Json {
            encodeDefaults = true
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

    public val publicDB: Database = Database("public")
    public val privateDB: Database = Database("private")
    public val sharedDB: Database = Database("shared")

    public inner class Database internal constructor(internal val name: String) {
        // temp fix
        public suspend fun lookup(
            lookupRecord: Lookup.Request
        ): List<SomeUser> =
            request("/users/lookup/email", Lookup.Request.serializer()) {
                lookupRecord
            }.let {
                json.decodeFromString(Lookup.Response.serializer(), it)
            }.users

        public suspend fun <T : Record> query(
            recordInformation: Record.Information<T>,
            zoneID: ZoneID? = ZoneID.default,
            filter: List<Filter>? = null,
            sort: List<Sort>? = null
        ): List<T> = request("/records/query", Request.serializer()) {
            Request(
                zoneID = zoneID,
                query = Query(recordType = recordInformation.recordType, filterBy = filter, sortBy = sort)
            )
        }.toRegularResponse(recordInformation)
    }

    private suspend fun <In> Database.request(
        resource: String,
        serializer: KSerializer<In>,
        requestBody: () -> In
    ): String {
        val subPath = "/database/1/$container/$environment/$name$resource"

        val date = Clock.System.now().toString().dropLastWhile { it != '.' }.replace('.', 'Z')
        val body = json.encodeToString(serializer, requestBody())

        val bodySignature = sha256(body).encodeBase64
        val signature = ecdsa(privateECPrime256v1Key, "$date:$bodySignature:$subPath")
        return client.post(subPath) {
            header("X-Apple-CloudKit-Request-ISO8601Date", date)
            header("X-Apple-CloudKit-Request-SignatureV1", signature)
            this.body = body
        }
    }

    private fun <T : Record> String.toRegularResponse(recordInformation: Record.Information<T>) = let {
        println(it)
        json.decodeFromString(Response.serializer(recordInformation.serializer()), it)
    }.records
}
