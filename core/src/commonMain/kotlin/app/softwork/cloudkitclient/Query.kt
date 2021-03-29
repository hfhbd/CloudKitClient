package app.softwork.cloudkitclient

import kotlinx.serialization.*

@Serializable
internal data class Query(val recordType: String, val filterBy: List<Filter>? = null, val sortBy: List<Sort>? = null)