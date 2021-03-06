package app.softwork.cloudkitclient

import kotlinx.serialization.*

@Serializable
public data class User(
    @SerialName("userRecordName")
    override val recordName: String,
    override val firstName: String? = null,
    override val lastName: String? = null,
    override val emailAddress: String? = null,
    override val created: TimeInformation? = null,
    override val modified: TimeInformation? = null,
    override val deleted: Boolean? = null
) : UserRecord {
    public companion object : Record.Information<User> {
        override val recordType: String = "users"
    }

    /**
     * A dictionary that identifies a user with the following keys.
     */
    @Serializable
    public data class Identity(val lookupInfo: LookupInfo, val userRecordName: String, val nameComponents: Name) {

        /**
         * A dictionary used to lookup user information.
         * @param emailAddress A string representing user’s email address.
         * @param phoneNumber A string representing user’s phone number.
         * @param userRecordName The record name field in the associated [User] record.
         */
        @Serializable
        public data class LookupInfo(
            val emailAddress: String? = null,
            val phoneNumber: String? = null,
            val userRecordName: String
        )
    }
}
