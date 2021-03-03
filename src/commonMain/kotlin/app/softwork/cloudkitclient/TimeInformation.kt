@file:UseSerializers(InstantSerializer::class)

package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.serializer.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.uuid.UUID

@Serializable
public data class TimeInformation(
    val timestamp: Instant,
    val userRecordName: String,
    val deviceID: UUID
)
