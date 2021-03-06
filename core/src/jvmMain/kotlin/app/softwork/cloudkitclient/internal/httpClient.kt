package app.softwork.cloudkitclient.internal

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*

internal actual fun httpClient(config: HttpClientConfig<out HttpClientEngineConfig>.() -> Unit) =
    HttpClient(CIO, config)
