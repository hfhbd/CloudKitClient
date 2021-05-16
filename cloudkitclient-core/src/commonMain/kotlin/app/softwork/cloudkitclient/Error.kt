package app.softwork.cloudkitclient

import kotlinx.serialization.Serializable

@Serializable
public data class Error(
    val recordName: String? = null,
    val reason: String? = null,
    val serverErrorCode: String? = null,
    val retryAfter: Int? = null,
    val uuid: String? = null,
    val redirectURL: String? = null,
) : Exception()
