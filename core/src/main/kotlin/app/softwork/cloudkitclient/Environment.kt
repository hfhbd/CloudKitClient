package app.softwork.cloudkitclient

import kotlinx.serialization.Serializable

@Serializable
public enum class Environment {
    Development,
    Production;
    override fun toString(): String = name.lowercase()
}
