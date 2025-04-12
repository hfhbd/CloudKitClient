package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.values.Value
import kotlinx.serialization.Serializable
import kotlin.reflect.KProperty1

@Serializable
public data class Filter(
    val fieldName: String,
    val comparator: Comparator,
    val fieldValue: Value,
    val distance: Double? = null
) {

    public enum class SystemFieldNames(internal val fieldName: String, public val systemFieldName: String) {
        RecordName("___recordId", "recordName"),
        Share("___share", "share"),
        Parent("___parent", "parent"),
        CreatedBy("___createdBy", "createdUserRecordName"),
        CreationTime("___createTime", "createdTimestamp"),
        ModificationTime("___modTime", "modifiedTimestamp"),
        ModifiedBy("___modifiedBy", "modifiedUserRecordName")
    }

    @Serializable
    public enum class Comparator {
        EQUALS, NOT_EQUALS,
        LESS_THAN, LESS_THAN_OR_EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUALS,
        NEAR,

        CONTAINS_ALL_TOKENS,
        CONTAINS_ANY_TOKENS,

        IN, NOT_IN,

        LIST_CONTAINS,
        NOT_LIST_CONTAINS,
        NOT_LIST_CONTAINS_ANY,

        BEGINS_WITH,
        NOT_BEGINS_WITH,
        LIST_MEMBER_BEGINS_WITH,
        NOT_LIST_MEMBER_BEGINS_WITH,
        LIST_CONTAINS_ALL,
        NOT_LIST_CONTAINS_ALL
    }

    public class Builder<Fields> {

        public infix fun KProperty1<Fields, Value.String?>.eq(value: String) {
            filters.add(Filter(fieldName = name, comparator = Comparator.EQUALS, fieldValue = Value.String(value)))
        }

        public infix fun <TFields, TR : Record<TFields>> KProperty1<Fields, Value.Reference<TFields, TR>>.eq(value: TR) {
            filters.add(
                Filter(
                    fieldName = name,
                    comparator = Comparator.EQUALS,
                    fieldValue = Value.Reference(
                        Value.Reference.Ref<TFields, TR>(value.recordName)
                    )
                )
            )
        }

        private val filters = mutableListOf<Filter>()

        public fun build(): List<Filter> = filters
    }
}
