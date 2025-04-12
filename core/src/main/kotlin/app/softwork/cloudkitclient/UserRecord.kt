package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.Record.Information
import app.softwork.cloudkitclient.values.Value
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.reflect.KProperty1

@Serializable
@SerialName("users")
public data class UserRecord(
    @SerialName("userRecordName")
    override val recordName: String,
    override val fields: Fields,
    override var created: TimeInformation? = null,
    override var modified: TimeInformation? = null,
    override var deleted: Boolean? = null,
    override val pluginFields: PluginFields = PluginFields(),
    override var recordChangeTag: String? = null,
    override val zoneID: ZoneID = ZoneID.default
) : Record<UserRecord.Fields> {
    override val recordType: String = Companion.recordType

    public companion object : Information<Fields, UserRecord> {
        public override fun fields(): List<KProperty1<Fields, Value?>> = listOfNotNull(
            Fields::firstName,
            Fields::lastName,
            Fields::emailAddress
        )

        public override fun fieldsSerializer(): KSerializer<Fields> = Fields.serializer()
    }

    @Serializable
    public data class Fields(
        public val firstName: Value.String?,
        public val lastName: Value.String?,
        public val emailAddress: Value.String?
    )

    /**
     * A dictionary that identifies a user with the following keys.
     */
    @Serializable
    public data class Identity(val lookupInfo: LookupInfo, val userRecordName: String, val nameComponents: Name) {

        /**
         * A dictionary used to lookup user information.
         * @param emailAddress A string representing user’s email address.
         * @param phoneNumber A string representing user’s phone number.
         * @param userRecordName The record name field in the associated [UserRecord] record.
         */
        @Serializable
        public data class LookupInfo(
            val emailAddress: String? = null,
            val phoneNumber: String? = null,
            val userRecordName: String
        )
    }
}
