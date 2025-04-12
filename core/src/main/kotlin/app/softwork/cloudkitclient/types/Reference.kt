package app.softwork.cloudkitclient.types

import app.softwork.cloudkitclient.ZoneID
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Reference(
    val recordName: String,
    val zoneID: ZoneID? = null,
    val action: Action
) {
    @Serializable
    public enum class Action {
        @SerialName("None")
        None,

        @SerialName("DELETE_SELF")
        DeleteSelf,

        @SerialName("VALIDATE")
        Validate
    }
}
