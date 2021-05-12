package app.softwork.cloudkitclient

import kotlin.test.*

class TestClientTest {
    @Test
    fun init() {
        val client = TestClient()
        assertEquals("public", client.publicDB.name)
        assertEquals("private", client.privateDB.name)
        assertEquals("shared", client.sharedDB.name)

        listOf(client.publicDB, client.privateDB, client.sharedDB).forEach {
            with(it) {
                assertEquals(1, zones.size)
                assertTrue(ZoneID.default in zones.keys)
                val zone = zones.values.first()
                assertEquals(TestDatabase.initUser, zone.initUser)
                assertEquals(1, zone.storage.size)
            }
        }
    }

    @Test
    fun queryFind() {
        val client = TestClient()
        val defaultZone = client.publicDB.zones.values.first()
        val filter = Filter.Builder<UserRecord.Fields>().apply {
            UserRecord.Fields::firstName eq "Test"
        }.build()
        val sort = Sort.Builder<UserRecord.Fields>().apply {
            ascending(UserRecord.Fields::lastName)
        }.build()
        val results = defaultZone.query(UserRecord, filter, sort)
        assertEquals(1, results.size)
    }

    @Test
    fun queryNoFind() {
        val client = TestClient()
        val defaultZone = client.publicDB.zones.values.first()
        val filter = Filter.Builder<UserRecord.Fields>().apply {
            UserRecord.Fields::firstName eq "Foo"
        }.build()
        val sort = Sort.Builder<UserRecord.Fields>().apply {
            ascending(UserRecord.Fields::lastName)
        }.build()
        val results = defaultZone.query(UserRecord, filter, sort)
        assertEquals(0, results.size)
    }
}
