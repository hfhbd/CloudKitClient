package app.softwork.cloudkitclient

public enum class Environment {
    Development,
    Production;
    override fun toString(): String = name.lowercase()
}
