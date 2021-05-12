package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.Client.*
import app.softwork.cloudkitclient.Record.*
import app.softwork.cloudkitclient.types.Asset.Upload.*
import app.softwork.cloudkitclient.types.Asset.Upload.Response.*
import app.softwork.cloudkitclient.values.*

public open class TestDatabase(
    public val name: String,
    public val zones: Map<ZoneID, Storage> = mapOf(ZoneID.default to Storage(initUser))
) : Database {

    public companion object {
        public val initUser: UserRecord = UserRecord("TestUser", fields = UserRecord.Fields(Value.String("Test"), Value.String("User"), null))
    }

    public override suspend fun lookup(lookupRecord: Lookup.Request): List<Lookup.Response.SomeUser> {
        TODO("Not yet implemented")
    }

    public override suspend fun <F: Record.Fields, R : Record<F>> query(
        recordInformation: Information<F, R>,
        zoneID: ZoneID,
        filter: Filter.Builder<F>.() -> Unit,
        sort: Sort.Builder<F>.() -> Unit
    ): List<R> {
        return zones[zoneID]!!.query(recordInformation, Filter.Builder<F>().apply(filter).build(), Sort.Builder<F>().apply(sort).build())
    }

    private val <F: Record.Fields, R : Record<F>> R.zone get() = zones[zoneID] ?: error("Zone $zoneID not found")

    override suspend fun <F: Record.Fields, R : Record<F>> create(record: R, recordInformation: Information<F, R>): R =
        record.zone.create(record, recordInformation)


    override suspend fun <F: Record.Fields, R : Record<F>> update(record: R, recordInformation: Information<F, R>): R =
        record.zone.update(record, recordInformation)

    override suspend fun <F: Record.Fields, R : Record<F>> delete(record: R, recordInformation: Information<F, R>) {
        record.zone.delete(record, recordInformation)
    }

    override suspend fun upload(asset: ByteArray, field: Field, zoneID: ZoneID): List<Token> =
        zones[zoneID]!!.upload(asset, field)
}
