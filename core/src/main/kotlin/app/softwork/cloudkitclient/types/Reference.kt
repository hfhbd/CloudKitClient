package app.softwork.cloudkitclient.types

import app.softwork.cloudkitclient.*
import kotlinx.serialization.*

@Serializable
public data class Reference<F : Record.Fields, TargetRecord : Record<F>>(
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