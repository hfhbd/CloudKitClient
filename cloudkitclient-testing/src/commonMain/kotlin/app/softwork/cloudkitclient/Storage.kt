package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.Record.Fields
import app.softwork.cloudkitclient.Record.Information
import app.softwork.cloudkitclient.values.Value.Asset
import kotlin.reflect.*

public interface Storage {
    public fun randomChangeTag(): String
    public fun now(
        userRecordName: String,
        deviceID: String = "Testing"
    ): TimeInformation

    public fun <F : Fields, R : Record<F>> create(record: R, recordInformation: Information<F, R>): R
    public fun <F : Fields, R : Record<F>> get(
        recordName: String,
        recordInformation: Information<F, R>
    ): R?

    public fun <F : Fields, R : Record<F>> delete(record: R, recordInformation: Information<F, R>)
    public fun <F : Fields, R : Record<F>> update(record: R, recordInformation: Information<F, R>): R
    public fun <F : Fields, R : Record<F>> upload(
        content: ByteArray,
        recordInformation: Information<F, R>,
        field: KProperty1<F, Asset?>,
        recordName: String?
    ): app.softwork.cloudkitclient.types.Asset

    public fun <F : Fields, R : Record<F>> query(
        recordInformation: Information<F, R>,
        filters: List<Filter>,
        sorts: List<Sort>
    ): List<R>
}
