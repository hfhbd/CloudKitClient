package app.softwork.cloudkitclient

import kotlinx.coroutines.runBlocking

internal actual fun runTest(block: suspend () -> Unit) = runBlocking {
    block()
}