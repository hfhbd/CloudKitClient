package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.Record.*
import kotlinx.serialization.*

@Serializable
data class TodoRecord(
    override val recordName: String,

    override val created: TimeInformation? = null,
    override val modified: TimeInformation? = null,
    override val deleted: Boolean? = null,
    override val fields: Fields,
    override val pluginFields: PluginFields = PluginFields(),
    override val recordChangeTag: String? = null,
    override val zoneID: ZoneID = ZoneID.default
) : Record {
    override val recordType: String = Companion.recordType

    companion object : Information<TodoRecord> {
        override val recordType: String = "Todo"

        @Serializable
        data class Fields(val subtitle: Value): Record.Fields
    }
}
