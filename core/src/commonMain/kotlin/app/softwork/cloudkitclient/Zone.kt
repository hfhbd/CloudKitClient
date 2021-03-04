package app.softwork.cloudkitclient

import kotlinx.serialization.Serializable

@Serializable
public data class Zone(val zoneID: ZoneID, val syncToken: String, val atomic: Boolean)