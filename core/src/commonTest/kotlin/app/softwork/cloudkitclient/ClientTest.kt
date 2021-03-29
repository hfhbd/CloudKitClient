package app.softwork.cloudkitclient

import kotlin.test.*

class ClientTest {
    @Test
    fun creation() {
        val client = CKClient(keyID = "", privateECPrime256v1Key = "".encodeToByteArray(), container = "iCloud.")
        assertEquals("iCloud.", client.container)
    }
}
