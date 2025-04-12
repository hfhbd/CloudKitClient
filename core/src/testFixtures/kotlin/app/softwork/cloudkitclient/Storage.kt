package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.Record.Information
import app.softwork.cloudkitclient.values.Value.Asset
import kotlin.reflect.KProperty1

public interface Storage {
    public fun randomChangeTag(): String
    public fun now(
        userRecordName: String,
        deviceID: String = "Testing"
    ): TimeInformation

    public fun <Fields, R : Record<Fields>> create(record: R, recordInformation: Information<Fields, R>): R
    public fun <Fields, R : Record<Fields>> get(
        recordName: String,
        recordInformation: Information<Fields, R>
    ): R?

    public fun <Fields, R : Record<Fields>> delete(record: R, recordInformation: Information<Fields, R>)
    public fun <Fields, R : Record<Fields>> update(record: R, recordInformation: Information<Fields, R>): R
    public fun <Fields, R : Record<Fields>> upload(
        content: ByteArray,
        recordInformation: Information<Fields, R>,
        field: KProperty1<Fields, Asset?>,
        recordName: String?
    ): app.softwork.cloudkitclient.types.Asset

    public fun <Fields, R : Record<Fields>> query(
        recordInformation: Information<Fields, R>,
        filters: List<Filter>,
        sorts: List<Sort>
    ): List<R>
}
