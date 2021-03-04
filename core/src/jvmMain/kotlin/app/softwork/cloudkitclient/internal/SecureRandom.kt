package app.softwork.cloudkitclient.internal

import kotlin.random.*

internal actual val secureRandom: Random = java.security.SecureRandom().asKotlinRandom()