package app.softwork.cloudkitclient.values

import app.softwork.cloudkitclient.*
import kotlinx.datetime.*
import kotlinx.serialization.*
import kotlin.io.encoding.*

@Serializable
public sealed class Value {

    @Serializable
    @SerialName("ASSET")
    public data class Asset(val value: app.softwork.cloudkitclient.types.Asset) : Value()

    @Serializable
    @SerialName("BYTE")
    public data class Byte(
        @SerialName("value")
        private val base64Value: kotlin.String
    ) : Value() {
        @OptIn(ExperimentalEncodingApi::class)
        val value: ByteArray
            get() = Base64.decode(base64Value)
    }

    @Serializable
    @SerialName("LOCATION")
    public data class Location(val value: app.softwork.cloudkitclient.types.Location) : Value()


    @Serializable
    @SerialName("DOUBLE")
    public data class Double(val value: kotlin.Double) : Value()

    @Serializable
    @SerialName("INT")
    public data class Number(val value: Long) : Value()


    @Serializable
    @SerialName("REFERENCE")
    public data class Reference<F : Record.Fields, TargetRecord : Record<F>>(
        val value: Ref<F, TargetRecord>
    ) : Value() {
        public constructor(record: TargetRecord): this(Ref(recordName = record.recordName))

        @Serializable
        public data class Ref<F : Record.Fields, TargetRecord : Record<F>>(val recordName: kotlin.String)
    }


    @Serializable
    @SerialName("STRING")
    public data class String(val value: kotlin.String) : Value()


    @Serializable
    @SerialName("DATETIME")
    public data class DateTime(@SerialName("value") private val milliseconds: Long) : Value() {
        public val value: Instant get() = Instant.fromEpochMilliseconds(milliseconds)

        public constructor(instant: Instant) : this(instant.toEpochMilliseconds())
    }


    @Serializable
    @SerialName("LIST")
    public data class List(val value: kotlin.collections.List<Value>) : Value()
}
