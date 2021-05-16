package app.softwork.cloudkitclient.internal

import kotlin.test.*

class Base64Test {
    @Test
    fun simple() {
        assertEquals("SGVsbG9Xb3JsZA==", "HelloWorld".encodeBase64)
        assertEquals("HelloWorld", "SGVsbG9Xb3JsZA==".decodeBase64)
    }
}
