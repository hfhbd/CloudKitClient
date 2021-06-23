package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.Filter.Comparator.*
import app.softwork.cloudkitclient.Record.*
import app.softwork.cloudkitclient.values.*
import app.softwork.cloudkitclient.values.Value.*
import kotlinx.datetime.Clock.*
import kotlinx.uuid.*
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlin.reflect.*
import kotlin.time.*

public class Storage(
    public val initUser: UserRecord,
    private val assets: MutableMap<UUID, Pair<app.softwork.cloudkitclient.types.Asset, ByteArray>>
) {

    public val storage: MutableMap<String, Record<*>> = mutableMapOf()

    init {
        create(initUser, UserRecord)
    }

    public fun randomChangeTag(): String = UUID().toString().take(8)

    public fun now(
        userRecordName: String = initUser.recordName,
        deviceID: String = "Testing"
    ): TimeInformation = TimeInformation(
        timestamp = System.now(),
        userRecordName = userRecordName,
        deviceID = deviceID
    )

    public fun <F : Record.Fields, R : Record<F>> create(record: R, recordInformation: Information<F, R>): R {
        val changeTag = randomChangeTag()
        val now = now()
        record.recordChangeTag = changeTag
        record.created = now
        record.modified = now
        storage[record.recordName] = record
        return record
    }

    public fun <F : Record.Fields, R : Record<F>> get(
        recordName: String,
        recordInformation: Record.Information<F, R>
    ): R? {
        return storage[recordName]?.let { it as R }
    }

    public fun <F : Record.Fields, R : Record<F>> delete(record: R, recordInformation: Information<F, R>) {
        val key = record.recordName
        val oldRecord = storage[key]
        requireNotNull(oldRecord)
        require(oldRecord.recordChangeTag == record.recordChangeTag)
        storage.remove(key)
    }

    public fun <F : Record.Fields, R : Record<F>> update(record: R, recordInformation: Information<F, R>): R {
        val oldRecord = storage[record.recordName]
        requireNotNull(oldRecord)
        require(oldRecord.recordChangeTag == record.recordChangeTag)
        val changeTag = randomChangeTag()
        val now = now()
        record.recordChangeTag = changeTag
        record.modified = now
        storage[record.recordName] = record
        return record
    }

    public fun <F : Record.Fields, R : Record<F>> upload(
        content: ByteArray,
        recordInformation: Record.Information<F, R>,
        field: KProperty1<F, Value.Asset?>,
        recordName: String?
    ): app.softwork.cloudkitclient.types.Asset {
        val download = UUID()
        val asset = app.softwork.cloudkitclient.types.Asset(
            fileChecksum = "",
            size = content.size,
            receipt = "",
            downloadURL = download.toString()
        )
        assets[download] = asset to content

        return asset
    }

    @OptIn(ExperimentalTime::class)
    public fun <F : Record.Fields, R : Record<F>> query(
        recordInformation: Information<F, R>,
        filters: List<Filter>?,
        sorts: List<Sort>?
    ): List<R> = storage.values.filter { value ->
        value.recordType == recordInformation.recordType
    }.map {
        it as R
    }.filter { record ->
        filters?.let {
            val properties = recordInformation.fields()
            filters.all { filter ->
                val value = properties.value(record.fields, filter)
                value.check(filter)
            }
        } ?: true
    }.let {
        if (sorts != null && sorts.isNotEmpty()) {
            val properties = recordInformation.fields()
            var comparator = compareBy<R> { record ->
                val sort = sorts.first()
                properties.sort(sort, record)
            }

            for (sort in sorts.subList(1, sorts.size)) {
                comparator = if (sort.ascending) {
                    comparator.thenBy { record ->
                        properties.sort(sort, record)
                    }
                } else {
                    comparator.thenByDescending { record ->
                        properties.sort(sort, record)
                    }
                }
            }
            it.sortedWith(comparator)
        } else it
    }

    private fun <F : Fields, R : Record<F>> List<KProperty1<F, Value?>>.sort(
        sort: Sort,
        record: R
    ): Comparable<Value>? = single {
        it.name == sort.fieldName
    }.get(record.fields)?.asComparable()

    private fun Value.asComparable(): Comparable<Value> {
        return when (this) {
            is Value.Double -> comparable {
                require(it is Value.Double)
                value.compareTo(it.value)
            }
            is Value.String -> comparable {
                require(it is Value.String)
                value.compareTo(it.value)
            }
            is Value.DateTime -> comparable {
                require(it is Value.DateTime)
                value.compareTo(it.value)
            }
            else -> error("Not comparable")
        }
    }

    private fun <T> T.comparable(comparing: T.(T) -> Int): Comparable<T> = object : Comparable<T> {
        override fun compareTo(other: T): Int = comparing(other)
    }

    private fun <F : Fields> List<KProperty1<F, Value?>>.value(
        fields: F,
        filter: Filter
    ): Value {
        val property: KProperty1<F, Value?> = single { prop ->
            prop.name == filter.fieldName
        }
        return property.get(fields) ?: error("property with filterName ${filter.fieldName} was null")
    }

    private fun Value.check(filter: Filter): Boolean {
        val otherValue = filter.fieldValue
        val comparator = filter.comparator
        return when (this) {
            is Value.Asset -> error("Not comparable")
            is Value.Byte -> error("Not comparable")
            is Value.DateTime -> {
                require(otherValue is DateTime)
                when (comparator) {
                    EQUALS -> value == otherValue.value
                    NOT_EQUALS -> value != otherValue.value
                    LESS_THAN -> value < otherValue.value
                    LESS_THAN_OR_EQUALS -> value <= otherValue.value
                    GREATER_THAN -> value > otherValue.value
                    GREATER_THAN_OR_EQUALS -> value >= otherValue.value
                    else -> error("Not supported")
                }
            }
            is Value.Double -> {
                val other = when (otherValue) {
                    is Value.Double -> otherValue.value
                    is Value.Number -> otherValue.value.toDouble()
                    else -> error("Not comparable")
                }
                when (comparator) {
                    EQUALS -> value == other
                    NOT_EQUALS -> value != other
                    LESS_THAN -> value < other
                    LESS_THAN_OR_EQUALS -> value <= other
                    GREATER_THAN -> value > other
                    GREATER_THAN_OR_EQUALS -> value >= other
                    else -> error("Not supported")
                }
            }
            is Value.Number -> {
                val other = when (otherValue) {
                    is Value.Double -> otherValue.value.toLong()
                    is Value.Number -> otherValue.value
                    else -> error("Not comparable")
                }
                when (comparator) {
                    EQUALS -> value == other
                    NOT_EQUALS -> value != other
                    LESS_THAN -> value < other
                    LESS_THAN_OR_EQUALS -> value <= other
                    GREATER_THAN -> value > other
                    GREATER_THAN_OR_EQUALS -> value >= other
                    else -> error("Not supported")
                }
            }
            is Value.Location -> TODO()
            is Value.Reference<*, *> -> {
                require(otherValue is Reference<*, *>)
                when (comparator) {
                    EQUALS -> value.recordName == otherValue.value.recordName
                    NOT_EQUALS -> value.recordName != otherValue.value.recordName
                    LESS_THAN -> TODO()
                    LESS_THAN_OR_EQUALS -> TODO()
                    GREATER_THAN -> TODO()
                    GREATER_THAN_OR_EQUALS -> TODO()
                    NEAR -> TODO()
                    CONTAINS_ALL_TOKENS -> TODO()
                    CONTAINS_ANY_TOKENS -> TODO()
                    IN -> TODO()
                    NOT_IN -> TODO()
                    LIST_CONTAINS -> TODO()
                    NOT_LIST_CONTAINS -> TODO()
                    NOT_LIST_CONTAINS_ANY -> TODO()
                    BEGINS_WITH -> TODO()
                    NOT_BEGINS_WITH -> TODO()
                    LIST_MEMBER_BEGINS_WITH -> TODO()
                    NOT_LIST_MEMBER_BEGINS_WITH -> TODO()
                    LIST_CONTAINS_ALL -> TODO()
                    NOT_LIST_CONTAINS_ALL -> TODO()
                }
            }
            is Value.String -> {
                val other = when (otherValue) {
                    is Value.String -> otherValue.value
                    is Value.Double -> otherValue.value.toString()
                    is Value.Number -> otherValue.value.toString()
                    else -> error("Not comparable")
                }
                when (comparator) {
                    EQUALS -> value == other
                    NOT_EQUALS -> value != other
                    LESS_THAN -> value < other
                    LESS_THAN_OR_EQUALS -> value <= other
                    GREATER_THAN -> value > other
                    GREATER_THAN_OR_EQUALS -> value >= other
                    CONTAINS_ALL_TOKENS -> value.split(" ").all { it == other }
                    CONTAINS_ANY_TOKENS -> value.split(" ").any { it == other }
                    else -> error("Not supported")
                }
            }
            is Value.List -> {
                val other = when (otherValue) {
                    is Value.String -> otherValue.value
                    is Value.Double -> otherValue.value.toString()
                    is Value.Number -> otherValue.value.toString()
                    else -> error("Not comparable")
                }
                when (comparator) {
                    //LIST_CONTAINS -> value.contains(other)
                    NOT_LIST_CONTAINS -> TODO()
                    NOT_LIST_CONTAINS_ANY -> TODO()
                    BEGINS_WITH -> TODO()
                    NOT_BEGINS_WITH -> TODO()
                    LIST_MEMBER_BEGINS_WITH -> TODO()
                    NOT_LIST_MEMBER_BEGINS_WITH -> TODO()
                    LIST_CONTAINS_ALL -> TODO()
                    NOT_LIST_CONTAINS_ALL -> TODO()
                    else -> error("Not supported")
                }
            }
        }
    }
}