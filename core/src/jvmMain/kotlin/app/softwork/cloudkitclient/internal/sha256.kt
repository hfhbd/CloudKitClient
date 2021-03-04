package app.softwork.cloudkitclient.internal

import java.security.MessageDigest
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal actual suspend fun sha256(content: String) = suspendCoroutine<String> { cont ->
    val value = MessageDigest.getInstance("SHA-256")
        .digest(content.toByteArray())
        .joinToString("") { "%02x".format(it) }
    cont.resume(value)
}