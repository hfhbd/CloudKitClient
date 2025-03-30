package app.softwork.cloudkitclient.serializer

import kotlin.time.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal data object InstantEpochSecondsSerializer : KSerializer<Instant> {
    override val descriptor = PrimitiveSerialDescriptor(
        "app.softwork.cloudkitclient.serializer.InstantEpochSecondsSerializer",
        PrimitiveKind.STRING,
    )

    override fun deserialize(decoder: Decoder) =
        Instant.fromEpochSeconds(decoder.decodeLong())

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeLong(value.epochSeconds)
    }
}
