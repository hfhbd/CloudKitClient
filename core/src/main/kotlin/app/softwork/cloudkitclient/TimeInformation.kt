@file:UseSerializers(InstantEpochSecondsSerializer::class)

package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.serializer.InstantEpochSecondsSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlin.time.Instant

@Serializable
public data class TimeInformation(
    val timestamp: Instant,
    val userRecordName: String,
    val deviceID: String
)
