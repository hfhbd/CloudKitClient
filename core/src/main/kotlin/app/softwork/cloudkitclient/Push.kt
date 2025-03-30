package app.softwork.cloudkitclient

import kotlinx.serialization.Serializable

public object Push {

    @Serializable
    public data class Create(val apnsEnvironment: Environment)

    @Serializable
    public data class Response(val apnsEnvironment: Environment, val apnsToken: String, val webcourierURL: String)
}
