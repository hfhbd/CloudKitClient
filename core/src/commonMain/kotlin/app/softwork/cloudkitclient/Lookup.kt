package app.softwork.cloudkitclient

import kotlinx.serialization.*

@Serializable
public data class Lookup(
    val records: List<Record>,
    val zoneID: ZoneID,
    val desiredKeys: List<String>? = null,
    val numbersAsStrings: Boolean = false
) {
    public companion object {
        public fun byMail(vararg mail: String): Request {
            return Request(
                users = listOf(
                    app.softwork.cloudkitclient.Lookup.Request.Mail(
                        emailAddress = mail.toList().toString()
                    )
                )
            )
        }
    }

    @Serializable
    public data class Record(val recordName: String, val desiredKeys: List<String>? = null)

    @Serializable
    public data class Response(val users: List<SomeUser>) {
        @Serializable
        public data class SomeUser(val emailAddress: String)
    }

    @Serializable
    public data class Request(val users: List<Mail>) {
        @Serializable
        public data class Mail(val emailAddress: String)
    }
}
