package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.values.Value
import kotlinx.serialization.KSerializer
import kotlin.reflect.KProperty1

public interface Record<Fields> {
    /**
     * The unique record ID. Often a UUID
     */
    public val recordName: String
    public val recordType: String

    public val fields: Fields

    public val pluginFields: PluginFields

    public var recordChangeTag: String?
    public var created: TimeInformation?
    public var modified: TimeInformation?
    public var deleted: Boolean?

    public val zoneID: ZoneID

    public interface Information<Fields, R : Record<Fields>> {
        /**
         * The static recordType
         */
        public val recordType: String
            get() = serializer().descriptor.serialName

        public fun serializer(): KSerializer<R>

        public fun fieldsSerializer(): KSerializer<Fields>

        public fun fields(): List<KProperty1<Fields, Value?>>
    }
}
