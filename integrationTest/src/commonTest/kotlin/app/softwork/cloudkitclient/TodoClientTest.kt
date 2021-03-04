package app.softwork.cloudkitclient

import io.ktor.utils.io.core.*
import kotlin.test.*

class TodoClientTest {
    @Test
    fun lookup() = runTest {
        val client = CKClient(keyID = "", privateECPrime256v1Key = "".toByteArray(), container = "iCloud.")
        val users =
            client.publicDB.lookup(Lookup.Request(users = listOf(Lookup.Request.Mail(listOf("foo@example.com")))), User)
        assertTrue(users.isEmpty())
    }
}