package app.softwork.cloudkitclient.internal

import kotlinx.coroutines.runBlocking

internal actual fun runTest(block: suspend () -> Unit) = runBlocking {
    block()
}
