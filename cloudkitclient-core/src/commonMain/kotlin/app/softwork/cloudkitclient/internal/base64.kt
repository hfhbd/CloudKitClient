package app.softwork.cloudkitclient.internal

internal expect val String.encodeBase64: String

internal expect val String.decodeBase64: String

internal expect val String.decodeBase64Bytes: ByteArray

internal expect val ByteArray.decodeBase64: String

internal expect val ByteArray.encodeBase64: String
