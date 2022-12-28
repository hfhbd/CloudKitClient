package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.types.*
import kotlinx.datetime.*
import kotlinx.uuid.*

public class TestClient(clock: Clock) : Client {
    override val logging: (String) -> Unit = { println(it) }
    private val assets: MutableMap<UUID, Pair<Asset, ByteArray>> = mutableMapOf()

    override val publicDB: TestDatabase = TestDatabase("public", assets, clock)
    override val privateDB: TestDatabase = TestDatabase("private", assets, clock)
    override val sharedDB: TestDatabase = TestDatabase("shared", assets, clock)

    override suspend fun download(assetToDownload: Asset): ByteArray =
        assets[assetToDownload.downloadURL!!.toUUID()]!!.second
}
