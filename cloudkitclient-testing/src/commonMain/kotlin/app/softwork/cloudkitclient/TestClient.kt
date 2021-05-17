package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.types.*
import kotlinx.uuid.*

public open class TestClient : Client {
    override val logging: (String) -> Unit = { println(it) }
    private val assets: MutableMap<UUID, Pair<Asset, ByteArray>> =
        mutableMapOf()

    override val publicDB: TestDatabase = TestDatabase("public", assets)
    override val privateDB: TestDatabase = TestDatabase("private", assets)
    override val sharedDB: TestDatabase = TestDatabase("shared", assets)

    override suspend fun download(assetToDownload: Asset): ByteArray =
        assets[assetToDownload.downloadURL!!.toUUID()]!!.second
}
