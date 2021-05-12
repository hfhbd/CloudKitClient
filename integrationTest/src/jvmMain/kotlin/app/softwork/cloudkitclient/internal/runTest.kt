package app.softwork.cloudkitclient.internal

import app.softwork.cloudkitclient.*
import kotlinx.coroutines.*

internal actual fun runTest(clients: List<Client>, block: suspend (Client) -> Unit) = runBlocking {
    clients.forEach {
        println(it)
        block(it)
    }
}
