@file:UseSerializers(InstantEpochSecondsSerializer::class)

package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.serializer.*
import kotlin.time.Instant
import kotlinx.serialization.*

@Serializable
public data class TimeInformation(
    val timestamp: Instant,
    val userRecordName: String,
    val deviceID: String
)
