package app.softwork.cloudkitclient.internal

import kotlinx.browser.window
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get

internal actual suspend fun sha256(content: String): String {
    val msgBuffer = TextEncoder().encode(content)
    val hashBuffer = window.crypto.subtle.digest("SHA-256", msgBuffer.buffer)
    val hashArray = Uint8Array(hashBuffer).let {
        Array(it.length) { i ->
            it[i]
        }
    }

    return hashArray.joinToString("") { b ->
        b.toString(16).padStart(2, '0')
    }
}

private external class TextEncoder {
    fun encode(data: String): Uint8Array
}