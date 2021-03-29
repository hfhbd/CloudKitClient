package app.softwork.cloudkitclient

import kotlinx.serialization.*

@Serializable
public data class UserRecordFields(
    public val firstName: String?,
    public val lastName: String?,
    public val emailAddress: String?
) : Record.Fields
