package app.softwork.cloudkitclient.internal

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.java.Java

internal fun httpClient(config: HttpClientConfig<out HttpClientEngineConfig>.() -> Unit) =
    HttpClient(Java, config)
