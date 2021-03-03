package app.softwork.cloudkitclient

import kotlinx.serialization.Serializable

@Serializable
data class TodoRecord(override val recordName: String) : Record {
    companion object : Record.Information<TodoRecord> {
        override val recordType: String = "Todo"
    }
}
