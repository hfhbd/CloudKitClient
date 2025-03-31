package app.softwork.cloudkitclient

import kotlinx.serialization.Serializable

@Serializable
public data class Response<F : Record.Fields, R : Record<F>>(
    val records: List<R>,
    val continuationMarker: String? = null
)
