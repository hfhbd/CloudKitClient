package app.softwork.cloudkitclient.types

import kotlinx.serialization.Serializable

@Serializable
public data class Asset(
    val fileChecksum: String,
    val size: Int,
    val referenceChecksum: String? = null,
    val wrappingKey: String? = null,
    val receipt: String,
    val downloadURL: String? = null
)