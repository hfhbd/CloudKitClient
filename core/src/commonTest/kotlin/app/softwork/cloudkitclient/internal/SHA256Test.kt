package app.softwork.cloudkitclient.internal

import kotlin.test.*

internal class SHA256Test {
    @Test
    fun testing() = runTest {
        assertEquals(
            "hy5OUM6ZkNiwQTMMR8nd0Rvsa1A66ThqmdqFhOm7EsQ=",
            sha256("HelloWorld").encodeBase64
        )
        assertEquals(
            "kKYFafHwahR0HRHPdqNWJlaQatR1SwdtYkWREl4QNUg=",
            sha256("{\"users\":[{\"emailAddress\":[\"foo@example.com\"]}]}").encodeBase64
        )
        assertEquals(
            "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
            sha256("").joinToString(separator = "") {
                (0xFF and it.toInt()).toString(16).padStart(2, '0')
            })
    }
}
