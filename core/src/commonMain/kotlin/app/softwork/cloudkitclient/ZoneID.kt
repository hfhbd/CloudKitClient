package app.softwork.cloudkitclient

import kotlinx.serialization.Serializable

@Serializable
public data class ZoneID(val zoneName: String, val ownerRecordName: String? = null) {
    public companion object {
        public val default: ZoneID = ZoneID("_defaultZone")
    }
}

