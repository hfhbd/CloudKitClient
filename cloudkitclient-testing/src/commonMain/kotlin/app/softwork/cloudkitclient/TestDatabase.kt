package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.Client.*
import app.softwork.cloudkitclient.Environment.*
import app.softwork.cloudkitclient.Push.Response
import app.softwork.cloudkitclient.Record.*
import app.softwork.cloudkitclient.types.*
import app.softwork.cloudkitclient.values.*
import kotlinx.uuid.*
import kotlin.reflect.*

public open class TestDatabase(
    public val name: String,
    public val assets: MutableMap<UUID, Pair<Asset, ByteArray>>,
    public val zones: Map<ZoneID, Storage> = mapOf(ZoneID.default to Storage(initUser, assets))
) : Database {

    public companion object {
        public val initUser: UserRecord = UserRecord("TestUser", fields = UserRecord.Fields(Value.String("Test"), Value.String("User"), null))
    }

    public override suspend fun <F: Record.Fields, R : Record<F>> query(
        recordInformation: Information<F, R>,
        zoneID: ZoneID,
        sort: Sort.Builder<F>.() -> Unit,
        filter: Filter.Builder<F>.() -> Unit
    ): List<R> {
        return zones[zoneID]!!.query(recordInformation, Filter.Builder<F>().apply(filter).build(), Sort.Builder<F>().apply(sort).build())
    }

    private val <F: Record.Fields, R : Record<F>> R.zone: Storage get() = zones[zoneID] ?: error("Zone $zoneID not found")

    override suspend fun <F: Record.Fields, R : Record<F>> create(record: R, recordInformation: Information<F, R>): R =
        record.zone.create(record, recordInformation)

    override suspend fun <F : Fields, R : Record<F>> read(
        recordName: String,
        recordInformation: Information<F, R>,
        zoneID: ZoneID
    ): R? = zones[zoneID]!!.get(recordName, recordInformation)

    override suspend fun <F: Record.Fields, R : Record<F>> update(record: R, recordInformation: Information<F, R>): R =
        record.zone.update(record, recordInformation)

    override suspend fun <F: Record.Fields, R : Record<F>> delete(record: R, recordInformation: Information<F, R>) {
        record.zone.delete(record, recordInformation)
    }

    override suspend fun <F : Fields, R : Record<F>> upload(
        asset: ByteArray,
        recordInformation: Record.Information<F, R>,
        field: KProperty1<F, Value.Asset?>,
        recordName: String?,
        zoneID: ZoneID
    ): Asset = zones[zoneID]!!.upload(asset, recordInformation, field, recordName)

    override suspend fun createToken(): Response {
        return Response(apnsEnvironment = Development, apnsToken = UUID().toString(), webcourierURL = "")
    }
}
