package app.softwork.cloudkitclient

import kotlinx.serialization.*

@Serializable
internal data class Request(
    val zoneID: ZoneID?,
    val resultsLimit: Int = 200,
    val query: Query,
    val continuationMarker: String? = null,
    val desiredKeys: List<String>? = null,
    val numbersAsStrings: Boolean = false,
    val zoneWide: Boolean = true
)