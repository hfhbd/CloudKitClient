package app.softwork.cloudkitclient.internal

import kotlin.io.encoding.*
import kotlin.test.*

@OptIn(ExperimentalEncodingApi::class)
internal class SHA256Test {
    @Test
    fun testing() {
        assertEquals(
            "hy5OUM6ZkNiwQTMMR8nd0Rvsa1A66ThqmdqFhOm7EsQ=",
            Base64.encode(sha256("HelloWorld"))
        )
        assertEquals(
            "kKYFafHwahR0HRHPdqNWJlaQatR1SwdtYkWREl4QNUg=",
            Base64.encode(sha256("{\"users\":[{\"emailAddress\":[\"foo@example.com\"]}]}"))
        )
        assertEquals(
            "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
            sha256("").joinToString(separator = "") {
                (0xFF and it.toInt()).toString(16).padStart(2, '0')
            })
    }
}
