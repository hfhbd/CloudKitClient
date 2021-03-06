package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.Value.*
import kotlinx.serialization.*

/**
 * A sort descriptor dictionary determines the order of the fetched records.
 *
 * @param fieldName The name of a field belonging to the record type. Used to sort the fetched records. This key is required.
 * @param ascending A Boolean value that indicates whether the fetched records should be sorted in ascending order. If true, the records are sorted in ascending order. If false, the records are sorted in descending order. The default value is true.
 * @param relativeLocation Records are sorted based on their distance to this location. Used only if fieldName is a Location type.
 */
@Serializable
public data class Sort(
    val fieldName: String,
    val ascending: Boolean = true,
    val relativeLocation: Location? = null
)