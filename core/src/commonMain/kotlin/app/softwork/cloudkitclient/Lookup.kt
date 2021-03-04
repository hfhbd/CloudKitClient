package app.softwork.cloudkitclient

import kotlinx.serialization.Serializable

@Serializable
public data class Lookup(
    val records: List<Record>,
    val zoneID: ZoneID,
    val desiredKeys: List<String>? = null,
    val numbersAsStrings: Boolean = false
) {
    @Serializable
    public data class Record(val recordName: String, val desiredKeys: List<String>? = null)

    @Serializable
    public data class Request(val users: List<Mail>) {
        @Serializable
        public data class Mail(val emailAddress: List<String>)
    }
}