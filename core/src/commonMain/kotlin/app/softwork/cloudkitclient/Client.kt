package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.Lookup.Request
import app.softwork.cloudkitclient.Lookup.Response.*
import app.softwork.cloudkitclient.Record.*
import app.softwork.cloudkitclient.Sort.*
import app.softwork.cloudkitclient.types.Asset.Upload.*
import app.softwork.cloudkitclient.types.Asset.Upload.Response.*

public interface Client {
    public val publicDB: Database
    public val privateDB: Database
    public val sharedDB: Database

    public interface Database {
        public suspend fun lookup(
            lookupRecord: Request
        ): List<SomeUser>

        public suspend fun <F: Fields, R : Record<F>> query(
            recordInformation: Information<F, R>,
            zoneID: ZoneID = ZoneID.default,
            filter: Filter.Builder<F>.() -> Unit = { },
            sort: Builder<F>.() -> Unit = { }
        ): List<R>

        public suspend fun <F: Fields, R : Record<F>> create(record: R, recordInformation: Information<F, R>): R
        public suspend fun <F: Fields, R : Record<F>> update(record: R, recordInformation: Information<F, R>): R
        public suspend fun <F: Fields, R : Record<F>> delete(record: R, recordInformation: Information<F, R>)

        public suspend fun upload(asset: ByteArray, field: Field, zoneID: ZoneID = ZoneID.default): List<Token>
    }
}
