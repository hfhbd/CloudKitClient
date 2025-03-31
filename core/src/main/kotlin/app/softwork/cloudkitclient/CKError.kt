package app.softwork.cloudkitclient

import kotlinx.serialization.Serializable

@Serializable
public data class CKError(
    val recordName: String? = null,
    val reason: String,
    val serverErrorCode: String,
    val retryAfter: Int? = null,
    val uuid: String? = null,
    val redirectURL: String? = null,
) : Exception()
