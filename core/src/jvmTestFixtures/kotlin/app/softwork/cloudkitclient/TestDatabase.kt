package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.Client.Database
import app.softwork.cloudkitclient.Environment.Development
import app.softwork.cloudkitclient.Push.Response
import app.softwork.cloudkitclient.Record.Fields
import app.softwork.cloudkitclient.Record.Information
import app.softwork.cloudkitclient.types.*
import app.softwork.cloudkitclient.values.*
import kotlin.time.*
import kotlin.reflect.*
import kotlin.uuid.*

@OptIn(ExperimentalUuidApi::class)
public class TestDatabase(
    public val name: String,
    public val assets: MutableMap<Uuid, Pair<Asset, ByteArray>>,
    clock: Clock,
    public val zones: Map<ZoneID, Storage> = mapOf(ZoneID.default to TestStorage(initUser, assets, clock))
) : Database {

    public companion object {
        public val initUser: UserRecord = UserRecord(
            recordName = "TestUser", 
            fields = UserRecord.Fields(
                firstName = Value.String("Test"),
                lastName = Value.String("User"), 
                emailAddress = null
            )
        )
    }

    public override suspend fun <F : Fields, R : Record<F>> query(
        recordInformation: Information<F, R>,
        zoneID: ZoneID,
        sort: Sort.Builder<F>.() -> Unit,
        filter: Filter.Builder<F>.() -> Unit
    ): List<R> = zones[zoneID]!!.query(
        recordInformation,
        Filter.Builder<F>().apply(filter).build(),
        Sort.Builder<F>().apply(sort).build()
    )

    private val <F : Fields, R : Record<F>> R.zone: Storage get() = zones[zoneID] ?: error("Zone $zoneID not found")

    override suspend fun <F : Fields, R : Record<F>> create(record: R, recordInformation: Information<F, R>): R =
        record.zone.create(record, recordInformation)

    override suspend fun <F : Fields, R : Record<F>> read(
        recordName: String,
        recordInformation: Information<F, R>,
        zoneID: ZoneID
    ): R? = zones[zoneID]!!.get(recordName, recordInformation)

    override suspend fun <F : Fields, R : Record<F>> update(record: R, recordInformation: Information<F, R>): R =
        record.zone.update(record, recordInformation)

    override suspend fun <F : Fields, R : Record<F>> delete(record: R, recordInformation: Information<F, R>) {
        record.zone.delete(record, recordInformation)
    }

    override suspend fun <F : Fields, R : Record<F>> upload(
        asset: ByteArray,
        recordInformation: Information<F, R>,
        field: KProperty1<F, Value.Asset?>,
        recordName: String?,
        zoneID: ZoneID
    ): Asset = zones[zoneID]!!.upload(asset, recordInformation, field, recordName)

    override suspend fun createToken(): Response =
        Response(apnsEnvironment = Development, apnsToken = Uuid.random().toString(), webcourierURL = "")
}
