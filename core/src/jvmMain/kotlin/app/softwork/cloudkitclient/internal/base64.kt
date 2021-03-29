package app.softwork.cloudkitclient.internal

import java.util.*

internal actual val String.encodeBase64: String get() =
    toByteArray().encodeBase64

internal actual val ByteArray.encodeBase64: String get() =
    Base64.getEncoder().encodeToString(this)

internal actual val String.decodeBase64: String get() =
    toByteArray().decodeBase64

internal actual val ByteArray.decodeBase64: String get() =
    String(Base64.getDecoder().decode(this))

internal actual val String.decodeBase64Bytes: ByteArray get() =
    Base64.getDecoder().decode(this)
