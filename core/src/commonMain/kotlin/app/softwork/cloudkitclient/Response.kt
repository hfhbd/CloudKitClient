package app.softwork.cloudkitclient

import kotlinx.serialization.Serializable

@Serializable
internal data class Response<T>(val records: List<T>, val continuationMarker: String)