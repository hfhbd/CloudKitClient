package app.softwork.cloudkitclient

import io.ktor.utils.io.core.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ClientTest {
    @Test
    fun creation() {
        val client = CKClient(keyID = "", privateECPrime256v1Key = "".toByteArray(), container = "iCloud.")
        assertEquals("/1/iCloud./development", client)
    }
}
