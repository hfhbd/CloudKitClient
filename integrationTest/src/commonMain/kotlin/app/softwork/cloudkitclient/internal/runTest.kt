package app.softwork.cloudkitclient.internal

import app.softwork.cloudkitclient.*

internal expect fun runTest(clients: List<Client>, block: suspend (Client) -> Unit)
