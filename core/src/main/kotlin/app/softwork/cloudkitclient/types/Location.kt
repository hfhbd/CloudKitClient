@file:UseSerializers(InstantEpochSecondsSerializer::class)

package app.softwork.cloudkitclient.types

import app.softwork.cloudkitclient.serializer.InstantEpochSecondsSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlin.time.Instant

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
