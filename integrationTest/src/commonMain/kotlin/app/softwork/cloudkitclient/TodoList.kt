package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.Record.*
import app.softwork.cloudkitclient.values.*
import kotlinx.serialization.*
import kotlin.reflect.*

data class TodoList(val name: String, val changeTag: String? = null)

@Serializable
data class TodoListRecord(
    override val recordName: String,
    override val fields: Fields = TodoListRecord.Fields(),

    override var created: TimeInformation? = null,
    override var modified: TimeInformation? = null,
    override var deleted: Boolean? = null,
    override val pluginFields: PluginFields = PluginFields(),
    override var recordChangeTag: String? = null,
    override val zoneID: ZoneID = ZoneID.default
) : Record<TodoListRecord.Fields> {
    override val recordType: String = Companion.recordType

    fun toDomain() = TodoList(
        name = recordName,
        changeTag = recordChangeTag
    )

    constructor(list: TodoList) : this(
        list.name,
        fields = Fields(),
        recordChangeTag = list.changeTag
    )

    companion object : Information<Fields, TodoListRecord> {
        override val recordType: String = "TodoList"

        override fun fields(): List<KProperty1<Fields, Value?>> = listOf()

        override fun fieldsSerializer() = Fields.serializer()
    }

    @Serializable
    class Fields: Record.Fields
}
