package app.softwork.cloudkitclient

import kotlinx.serialization.Serializable

@Serializable
public data class Response<Fields, R : Record<Fields>>(
    val records: List<R>,
    val continuationMarker: String? = null
)
