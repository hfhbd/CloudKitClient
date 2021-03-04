package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.internal.runTest
import app.softwork.cloudkitclient.internal.sha256
import kotlin.test.Test
import kotlin.test.assertEquals

internal class SHA256Test {
    @Test
    fun testing() = runTest {
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", sha256(""))
    }
}