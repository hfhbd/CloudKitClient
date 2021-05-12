package app.softwork.cloudkitclient.types

import app.softwork.cloudkitclient.*
import kotlinx.serialization.*

@Serializable
public data class Asset(
    val fileChecksum: String,
    val size: Int,
    val receipt: String,
    val referenceChecksum: String? = null,
    val wrappingKey: String? = null,
    val downloadURL: String? = null
) {
    @Serializable
    public data class Upload(val tokens: List<Field>, val zoneID: ZoneID = ZoneID.default) {
        @Serializable
        public data class Field(val fieldName: String, val recordType: String, val recordName: String? = null)

        @Serializable
        public data class Response(val tokens: List<Token>) {
            @Serializable
            public data class Token(val recordName: String, val fieldName: String, val url: String)
        }
    }
}
