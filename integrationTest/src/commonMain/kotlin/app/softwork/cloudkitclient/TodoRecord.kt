package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.Record.*
import app.softwork.cloudkitclient.types.*
import app.softwork.cloudkitclient.values.*
import kotlinx.serialization.*
import kotlinx.uuid.*
import kotlin.reflect.*

data class Todo(val id: UUID, val subtitle: String, val asset: Asset?, val changeTag: String? = null)

@Serializable
data class TodoRecord(
    override val recordName: String,
    override val fields: Fields,

    override var created: TimeInformation? = null,
    override var modified: TimeInformation? = null,
    override var deleted: Boolean? = null,
    override val pluginFields: PluginFields = PluginFields(),
    override var recordChangeTag: String? = null,
    override val zoneID: ZoneID = ZoneID.default
) : Record<TodoRecord.Fields> {
    override val recordType: String = Companion.recordType

    fun toDomain() = Todo(id = recordName.toUUID(), subtitle = fields.subtitle.value, asset = fields.asset?.value, changeTag = recordChangeTag)

    constructor(todo: Todo) : this(
        todo.id.toString(),
        fields = Fields(subtitle = Value.String(todo.subtitle)),
        recordChangeTag = todo.changeTag
    )

    companion object : Information<TodoRecord.Fields, TodoRecord> {
        override val recordType: String = "Todo"

        override fun fields() = listOf(Fields::subtitle)

        override fun fieldsSerializer() = Fields.serializer()
    }

    @Serializable
    data class Fields(val subtitle: Value.String, val asset: Value.Asset? = null) : Record.Fields
}
