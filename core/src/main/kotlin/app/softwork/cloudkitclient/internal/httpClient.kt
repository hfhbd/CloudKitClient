package app.softwork.cloudkitclient.internal

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.java.Java

internal fun httpClient(config: HttpClientConfig<out HttpClientEngineConfig>.() -> Unit) =
    HttpClient(Java, config)
