package app.softwork.cloudkitclient.internal

import io.ktor.client.*
import io.ktor.client.engine.*

internal expect fun httpClient(config: HttpClientConfig<out HttpClientEngineConfig>.() -> Unit): HttpClient