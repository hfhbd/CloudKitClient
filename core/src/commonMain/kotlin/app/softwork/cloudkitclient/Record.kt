package app.softwork.cloudkitclient

import kotlinx.serialization.*

public interface Record {
    /**
     * The unique record ID. Often an UUID
     */
    public val recordName: String
    public val recordType: String

    public val fields: Fields

    public val pluginFields: PluginFields

    public val recordChangeTag: String?

    public val created: TimeInformation?
    public val modified: TimeInformation?
    public val deleted: Boolean?

    public val zoneID: ZoneID

    public interface Information<T : Record> {
        /**
         * The static recordType
         */
        public val recordType: String

        public fun serializer(): KSerializer<T>
    }

    public interface Fields

    @Serializable
    public class PluginFields
}
