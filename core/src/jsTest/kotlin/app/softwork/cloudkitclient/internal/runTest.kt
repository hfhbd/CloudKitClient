package app.softwork.cloudkitclient.internal

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise

internal actual fun runTest(block: suspend () -> Unit): dynamic = GlobalScope.promise { block() }
