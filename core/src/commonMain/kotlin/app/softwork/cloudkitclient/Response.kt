package app.softwork.cloudkitclient

import kotlinx.serialization.*

@Serializable
internal data class Response<T>(
    val records: List<T>,
    val continuationMarker: String? = null
)