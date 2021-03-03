package app.softwork.cloudkitclient

import kotlinx.serialization.Serializable

@Serializable
public data class User(override val recordName: String): UserRecord {
    public companion object: Record.Information<User> {
        override val recordType: String = "users"
    }
}
