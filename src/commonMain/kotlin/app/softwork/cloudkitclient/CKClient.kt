@file:UseSerializers(InstantSerializer::class)

package app.softwork.cloudkitclient

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.json.Json
import app.softwork.cloudkitclient.serializer.InstantSerializer

public class CKClient(
    public val container: String,
    keyID: String,
    publicKey: String,
    privateKey: String,
    public val environment: Environment
) {

    private companion object {
        const val path = "https://api.apple-cloudkit.com"
    }

    private val json = Json {
        encodeDefaults = true
    }

    private val client = HttpClient {
        defaultRequest {
            //baseURL("https://api.apple-cloudkit.com/database/1/$container/$environment")
            header("X-Apple-CloudKit-Request-KeyID", keyID)
        }
    }

    // TODO: remove with baseURL feature
    private val serviceBaseURL: String = "database/1/$container/$environment"
    private val baseURL: String = "$path/$serviceBaseURL"

    public val publicDB: Database = Database("public")
    public val privateDB: Database = Database("private")
    public val sharedDB: Database = Database("shared")

    internal suspend fun <T : Record> request(db: Database, path: String, operation: Operation): List<T> {
        val signature: String = ""
        return client.post<String>("${baseURL}/${db.name}$path") {
            header("X-Apple-CloudKit-Request-ISO8601Date", Clock.System.now().toString())
            header("X-Apple-CloudKit-Request-SignatureV1", signature)
        }.let {
            json.decodeFromString(Response.serializer(recordInformation.serializer()), it)
        }.records
    }

    public inner class Database internal constructor(internal val name: String) {
        public suspend fun <T : Record> query(
            recordInformation: Record.Information<T>,
            zone: ZoneID? = ZoneID.default
        ): List<T> {
            val operation =
                QueryRequest(
                    query = listOf(Lockup.LookupRecord(recordName = recordInformation.recordType)),
                    zoneID = zone
                )
            val signature = ""
            return client.post<String>("${baseURL}/$name/records/query") {
                header("X-Apple-CloudKit-Request-ISO8601Date", Clock.System.now().toString())
                header("X-Apple-CloudKit-Request-SignatureV1", signature)
            }.let {
                json.decodeFromString(Response.serializer(recordInformation.serializer()), it)
            }.records
        }

        public suspend fun <T : Record> getAll(
            recordInformation: Record.Information<T>,
            zone: ZoneID? = ZoneID.default
        ): List<T> {

        }
    }
}

public interface Operation {

}

@Serializable
internal data class QueryRequest(
    val zoneID: ZoneID?,
    val resultsLimit: Int,
    val query: Query,
    val continuationMarker: String,
    val desiredKeys: List<String>? = null,
    val numbersAsStrings: Boolean = false
) : Operation {
    val zoneWide: Boolean = zoneID == null

    @Serializable
    data class Query(val recordType: String, val filterBy: List<Filter>, val sortBy: List<Sort>) {
        @Serializable
        data class Filter()

        @Serializable
        data class Sort()
    }
}

@Serializable
internal data class Response<T>(val records: List<T>, val continuationMarker: String)

@Serializable
internal data class Lockup(
    val records: List<LookupRecord>,
    val zoneID: ZoneID,
    val desiredKeys: List<String>? = null,
    val numbersAsStrings: Boolean = false
) {
    @Serializable
    internal data class LookupRecord(val recordName: String, val desiredKeys: List<String>? = null)
}

@Serializable
public data class ZoneID(val zoneName: String, val ownerRecordName: String? = null) {
    public companion object {
        public val default: ZoneID = ZoneID("_defaultZone")
    }
}

@Serializable
public data class Asset(
    val fileChecksum: String,
    val size: Int,
    val referenceChecksum: String? = null,
    val wrappingKey: String? = null,
    val receipt: String,
    val downloadURL: String? = null
)

@Serializable
public data class Location(
    val latitude: Double,
    val longitude: Double,
    val horizontalAccuracy: Double,
    val verticalAccuracy: Double,
    val altitude: Double,
    val speed: Double,
    val course: String,
    val timestamp: Instant
)
