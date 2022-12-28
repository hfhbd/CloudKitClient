package app.softwork.cloudkitclient

import kotlinx.datetime.*
import kotlin.test.*
import kotlin.time.*

@ExperimentalTime
class TestClientTest {
    @Test
    fun init() {
        val testTimeSource = TestTimeSource()
        val client = TestClient(testTimeSource.toClock())
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
        val client = TestClient(testTimeSource.toClock())
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
        val client = TestClient(testTimeSource.toClock())
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

    @ExperimentalTime
    private fun TimeSource.toClock(offset: Instant = Instant.fromEpochSeconds(0)): Clock = object : Clock {
        private val startMark: TimeMark = markNow()
        override fun now() = offset + startMark.elapsedNow()
    }
}
