package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.types.*
import io.ktor.util.*
import kotlinx.serialization.*

@Serializable
public sealed class Value {
    @Serializable
    @SerialName("asset")
    public data class AssetValue(val value: Asset) : Value()

    @Serializable
    @SerialName("boolean")
    public data class Bool(val value: Boolean) : Value()

    @Serializable
    @SerialName("bytes")
    public data class Byte(
        @SerialName("value")
        private val base64Value: String
    ) : Value() {
        @OptIn(InternalAPI::class)
        val value: ByteArray
            get() = base64Value.decodeBase64Bytes()
    }

    @Serializable
    @SerialName("Location")
    public data class Location(val value: app.softwork.cloudkitclient.types.Location) : Value()
}