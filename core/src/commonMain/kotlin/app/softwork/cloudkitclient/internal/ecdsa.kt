package app.softwork.cloudkitclient.internal

internal expect suspend fun ecdsa(key: ByteArray, data: String): String