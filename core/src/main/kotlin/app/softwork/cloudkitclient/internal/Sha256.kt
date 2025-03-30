package app.softwork.cloudkitclient.internal

import java.security.MessageDigest

internal fun sha256(content: String): ByteArray = MessageDigest.getInstance("SHA-256")
    .digest(content.toByteArray())
