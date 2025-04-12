package app.softwork.cloudkitclient

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
        public suspend fun <Fields, R : Record<Fields>> query(
            recordInformation: Information<Fields, R>,
            zoneID: ZoneID = ZoneID.default,
            sort: Builder<Fields>.() -> Unit = { },
            filter: Filter.Builder<Fields>.() -> Unit = { }
        ): List<R>

        public suspend fun <Fields, R : Record<Fields>> create(record: R, recordInformation: Information<Fields, R>): R
        public suspend fun <Fields, R : Record<Fields>> read(
            recordName: String,
            recordInformation: Information<Fields, R>,
            zoneID: ZoneID = ZoneID.default
        ): R?

        public suspend fun <Fields, R : Record<Fields>> update(record: R, recordInformation: Information<Fields, R>): R
        public suspend fun <Fields, R : Record<Fields>> delete(record: R, recordInformation: Information<Fields, R>)

        public suspend fun <Fields, R : Record<Fields>> upload(
            asset: ByteArray,
            recordInformation: Information<Fields, R>,
            field: KProperty1<Fields, Value.Asset?>,
            recordName: String?,
            zoneID: ZoneID = ZoneID.default
        ): Asset

        public suspend fun createToken(): Push.Response
    }
}
