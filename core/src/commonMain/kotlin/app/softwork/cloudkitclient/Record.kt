package app.softwork.cloudkitclient

import kotlinx.serialization.*

public interface Record {
    /**
     * The unique record ID. Often an UUID
     */
    public val recordName: String


    public val created: TimeInformation?
    public val modified: TimeInformation?
    public val deleted: Boolean?

    public interface Information<T : Record> {
        /**
         * The static recordType
         */
        public val recordType: String

        public fun serializer(): KSerializer<T>
    }
}
