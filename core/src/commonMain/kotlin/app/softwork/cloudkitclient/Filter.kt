package app.softwork.cloudkitclient

import kotlinx.serialization.*

@Serializable
public data class Filter(
    val fieldName: String,
    val comparator: Comparator,
    val fieldValue: Value,
    val distance: Double
) {
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
}