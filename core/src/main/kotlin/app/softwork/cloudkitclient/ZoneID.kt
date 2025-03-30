package app.softwork.cloudkitclient

import kotlinx.serialization.Serializable

@Serializable
public data class ZoneID(
    val zoneName: String,
    val ownerRecordName: String? = null,
    val zoneType: String = "DEFAULT_ZONE"
) {
    public companion object {
        public val default: ZoneID = ZoneID("_defaultZone")
    }
}
