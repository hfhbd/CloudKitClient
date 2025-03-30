package app.softwork.cloudkitclient

import kotlin.test.*
import kotlin.time.*

class TestClientTest {
    @Test
    fun init() {
        val testTimeSource = TestTimeSource()
        val client = TestClient(testTimeSource.asClock(origin = Instant.fromEpochSeconds(0)))
        assertEquals("public", client.publicDB.name)
        assertEquals("private", client.privateDB.name)
        assertEquals("shared", client.sharedDB.name)

        listOf(client.publicDB, client.privateDB, client.sharedDB).forEach {
            with(it) {
                assertEquals(1, zones.size)
                assertTrue(ZoneID.default in zones.keys)
                val zone = zones.values.first()
                assertEquals(
                    listOf(TestDatabase.initUser), 
                    zone.query(
                        UserRecord,
                        emptyList(),
                        emptyList(),
                    )
                )
            }
        }
    }

    @Test
    fun queryFound() {
        val testTimeSource = TestTimeSource()
        val client = TestClient(testTimeSource.asClock(origin = Instant.fromEpochSeconds(0)))
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
    fun queryNotFound() {
        val testTimeSource = TestTimeSource()
        val client = TestClient(testTimeSource.asClock(origin = Instant.fromEpochSeconds(0)))
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

    // https://youtrack.jetbrains.com/issue/KT-76394/kotlin.time.TimeSource.asClock-missing
    private fun TimeSource.asClock(origin: Instant): Clock = object : Clock {
        private val startMark: TimeMark = markNow()
        override fun now() = origin + startMark.elapsedNow()
    }
}
