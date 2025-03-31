package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.values.Value
import kotlinx.serialization.KSerializer
import kotlin.reflect.KProperty1

public interface Record<F : Record.Fields> {
    /**
     * The unique record ID. Often an UUID
     */
    public val recordName: String
    public val recordType: String

    public val fields: F

    public val pluginFields: PluginFields

    public var recordChangeTag: String?
    public var created: TimeInformation?
    public var modified: TimeInformation?
    public var deleted: Boolean?

    public val zoneID: ZoneID

    public interface Information<F : Fields, R : Record<F>> {
        /**
         * The static recordType
         */
        public val recordType: String

        public fun serializer(): KSerializer<R>

        public fun fieldsSerializer(): KSerializer<F>

        public fun fields(): List<KProperty1<F, Value?>>
    }

    public interface Fields
}
