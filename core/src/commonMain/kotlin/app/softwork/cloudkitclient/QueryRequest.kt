package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.types.Asset
import app.softwork.cloudkitclient.types.Location
import io.ktor.util.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
        public data class Filter(
            val fieldName: String,
            val comparator: Comparator,
            val fieldValue: Value,
            val distance: Double
        ) {
            @Serializable
            public enum class Comparator {
                EQUALS, NOT_EQUALS,
                LESS_THAN, LESS_THAN_OR_EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUALS,
                NEAR,

                CONTAINS_ALL_TOKENS,
                CONTAINS_ANY_TOKENS,

                IN, NOT_IN,

                LIST_CONTAINS,
                NOT_LIST_CONTAINS,
                NOT_LIST_CONTAINS_ANY,

                BEGINS_WITH,
                NOT_BEGINS_WITH,
                LIST_MEMBER_BEGINS_WITH,
                NOT_LIST_MEMBER_BEGINS_WITH,
                LIST_CONTAINS_ALL,
                NOT_LIST_CONTAINS_ALL
            }
        }

        /**
         * A sort descriptor dictionary determines the order of the fetched records.
         *
         * @param fieldName The name of a field belonging to the record type. Used to sort the fetched records. This key is required.
         * @param ascending A Boolean value that indicates whether the fetched records should be sorted in ascending order. If true, the records are sorted in ascending order. If false, the records are sorted in descending order. The default value is true.
         * @param relativeLocation Records are sorted based on their distance to this location. Used only if fieldName is a Location type.
         */
        @Serializable
        data class Sort(
            val fieldName: String,
            val ascending: Boolean = true,
            val relativeLocation: Value.Location? = null
        )
    }
}

@Serializable
public sealed class Value {
    @Serializable
    @SerialName("asset")
    internal data class AssetValue(val value: Asset) : Value()

    @Serializable
    @SerialName("boolean")
    internal data class Bool(val value: Boolean) : Value()

    @Serializable
    @SerialName("bytes")
    internal data class Byte(
        @SerialName("value")
        private val base64Value: String
    ) : Value() {
        @OptIn(InternalAPI::class)
        val value: ByteArray
            get() = base64Value.decodeBase64Bytes()
    }

    @Serializable
    @SerialName("Location")
    internal data class Location(val value: app.softwork.cloudkitclient.types.Location) : Value()
}