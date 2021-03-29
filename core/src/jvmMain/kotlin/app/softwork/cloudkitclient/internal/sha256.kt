package app.softwork.cloudkitclient.internal

import java.security.*

internal actual fun sha256(content: String): ByteArray = MessageDigest.getInstance("SHA-256")
    .digest(content.toByteArray())
