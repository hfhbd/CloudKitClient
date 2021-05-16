package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.types.*
import app.softwork.cloudkitclient.values.*
import kotlinx.serialization.*
import kotlin.reflect.*

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
) {
    public class Builder<F: Record.Fields> {
        private val sortedBy = mutableListOf<Sort>()

        public fun ascending(value: KProperty1<F, Value?>) {
            sortedBy.add(Sort(value.name, ascending = true))
        }

        public fun descending(value: KProperty1<F, Value?>) {
            sortedBy.add(Sort(value.name, ascending = false))
        }

        public infix fun KProperty1<F, Value.Location?>.relativeToLocation(to: Location) {
            sortedBy.add(Sort(fieldName = name, ascending = true, relativeLocation = to))
        }

        public fun build(): List<Sort> = sortedBy
    }
}
