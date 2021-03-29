package app.softwork.cloudkitclient

import kotlinx.serialization.*

@Serializable
internal data class Response<T: Record>(
    val records: List<T>,
    val continuationMarker: String? = null
)
