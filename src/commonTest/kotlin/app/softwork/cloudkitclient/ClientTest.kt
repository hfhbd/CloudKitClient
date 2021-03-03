package app.softwork.cloudkitclient

import kotlin.test.Test
import kotlin.test.assertEquals

class ClientTest {
    @Test
    fun creation() {
        val client = CKClient(keyID = "", privateKey = "", publicKey = "", container = "iCloud.", environment = Environment.Development)
        assertEquals("/1/iCloud./development", client)
    }

    @Test
    fun getRecords() = runTest {
        val client = CKClient(keyID = "", privateKey = "", publicKey = "", container = "iCloud.", environment = Environment.Development)
        val todos = client.publicDB.getAll(TodoRecord)
    }
}
