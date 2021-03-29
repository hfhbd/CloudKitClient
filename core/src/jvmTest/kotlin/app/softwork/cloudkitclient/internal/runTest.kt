package app.softwork.cloudkitclient.internal

import kotlinx.coroutines.*

internal actual fun runTest(block: suspend () -> Unit) = runBlocking {
    block()
}
