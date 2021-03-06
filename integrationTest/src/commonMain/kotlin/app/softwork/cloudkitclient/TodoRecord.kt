package app.softwork.cloudkitclient

import kotlinx.serialization.*

@Serializable
data class TodoRecord(
    override val recordName: String,
    override val created: TimeInformation? = null,
    override val modified: TimeInformation? = null,
    override val deleted: Boolean? = null
) : Record {
    companion object : Record.Information<TodoRecord> {
        override val recordType: String = "Todo"
    }
}
