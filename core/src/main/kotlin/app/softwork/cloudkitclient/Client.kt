package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.Record.Fields
import app.softwork.cloudkitclient.Record.Information
import app.softwork.cloudkitclient.Sort.Builder
import app.softwork.cloudkitclient.types.Asset
import app.softwork.cloudkitclient.values.Value
import kotlin.reflect.KProperty1

public interface Client {
    public val logging: (String) -> Unit

    public suspend fun download(assetToDownload: Asset): ByteArray

    public val publicDB: Database
    public val privateDB: Database
    public val sharedDB: Database

    public interface Database {
        public suspend fun <F : Fields, R : Record<F>> query(
            recordInformation: Information<F, R>,
            zoneID: ZoneID = ZoneID.default,
            sort: Builder<F>.() -> Unit = { },
            filter: Filter.Builder<F>.() -> Unit = { }
        ): List<R>

        public suspend fun <F : Fields, R : Record<F>> create(record: R, recordInformation: Information<F, R>): R
        public suspend fun <F : Fields, R : Record<F>> read(
            recordName: String,
            recordInformation: Information<F, R>,
            zoneID: ZoneID = ZoneID.default
        ): R?

        public suspend fun <F : Fields, R : Record<F>> update(record: R, recordInformation: Information<F, R>): R
        public suspend fun <F : Fields, R : Record<F>> delete(record: R, recordInformation: Information<F, R>)

        public suspend fun <F : Fields, R : Record<F>> upload(
            asset: ByteArray,
            recordInformation: Information<F, R>,
            field: KProperty1<F, Value.Asset?>,
            recordName: String?,
            zoneID: ZoneID = ZoneID.default
        ): Asset

        public suspend fun createToken(): Push.Response
    }
}
