package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.internal.ecdsa
import app.softwork.cloudkitclient.internal.sha256
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.datetime.Clock
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

public class CKClient(
    public val container: String,
    keyID: String,
    private val privateECPrime256v1Key: ByteArray,
    public val environment: Environment = Environment.Development
) {

    public companion object { }

    private val json = Json {
        encodeDefaults = true
    }

    private val client = HttpClient {
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "api.apple-cloudkit.com"
            }
            header("X-Apple-CloudKit-Request-KeyID", keyID)
        }
    }

    private fun Database.url(subPath: String): String = "/database/1/$container/$environment/$name/$subPath"

    public val publicDB: Database = Database("public")
    public val privateDB: Database = Database("private")
    public val sharedDB: Database = Database("shared")

    public inner class Database internal constructor(internal val name: String) {
        public suspend fun <T : Record> lookup(
            lookupRecord: Lookup.Request,
            recordInformation: Record.Information<T>,
            zone: ZoneID? = ZoneID.default
        ): List<T> {
            val subPath = url("/users/lookup/email")
            return client.post<String>(subPath) {
                prepare(subPath, lookupRecord, Lookup.Request.serializer())
            }.let {
                json.decodeFromString(Response.serializer(recordInformation.serializer()), it)
            }.records
        }
    }

    private suspend fun <T> HttpRequestBuilder.prepare(subPath: String, requestBody: T, serializer: KSerializer<T>) {
        val date = Clock.System.now().toString()
        val body = json.encodeToString(serializer, requestBody)

        @OptIn(InternalAPI::class)
        val bodySignature = sha256(body).decodeBase64String()
        val signature = ecdsa(privateECPrime256v1Key,"$date:$bodySignature:$subPath")

        header("X-Apple-CloudKit-Request-ISO8601Date", date)
        header("X-Apple-CloudKit-Request-SignatureV1", signature)
        this.body = body
    }
}
