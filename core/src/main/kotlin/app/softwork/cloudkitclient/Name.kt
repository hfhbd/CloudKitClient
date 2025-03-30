package app.softwork.cloudkitclient

import kotlinx.serialization.Serializable

/**
 * User's name representation.
 */
@Serializable
public data class Name(
    /** The user’s prefix. */
    val namePrefix: String,
    /** The user’s first name. */
    val givenName: String,
    /** The user’s last name. */
    val familyName: String,
    /** The user’s nickname. */
    val nickname: String,

    /** The user’s suffix. */
    val nameSuffix: String,

    /** The user’s middle name. */
    val middleName: String,

    /** A phonetic representation of the user’s name. */
    val phoneticRepresentation: String
)
