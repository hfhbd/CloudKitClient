package app.softwork.cloudkitclient.values

import app.softwork.cloudkitclient.Record
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.time.Instant

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
    public data class Double(val value: kotlin.Double) : Value(), Comparable<Double> {
        override fun compareTo(other: Double): Int = value.compareTo(other.value)
    }

    @Serializable
    @SerialName("INT")
    public data class Number(val value: Long) : Value()

    @Serializable
    @SerialName("REFERENCE")
    public data class Reference<Fields, TargetRecord : Record<Fields>>(
        val value: Ref<Fields, TargetRecord>
    ) : Value() {
        public constructor(record: TargetRecord) : this(Ref(recordName = record.recordName))

        @Serializable
        public data class Ref<Fields, TargetRecord : Record<Fields>>(val recordName: kotlin.String)
    }

    @Serializable
    @SerialName("STRING")
    public data class String(val value: kotlin.String) : Value(), Comparable<String> {
        override fun compareTo(other: String): Int = value.compareTo(other.value)
    }

    @Serializable
    @SerialName("DATETIME")
    public data class DateTime(@SerialName("value") private val milliseconds: Long) : Value(), Comparable<DateTime> {
        public val value: Instant get() = Instant.fromEpochMilliseconds(milliseconds)

        public constructor(instant: Instant) : this(instant.toEpochMilliseconds())

        override fun compareTo(other: DateTime): Int = value.compareTo(other.value)
    }

    @Serializable
    @SerialName("LIST")
    public data class List(val value: kotlin.collections.List<Value>) : Value()
}
