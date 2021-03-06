package app.softwork.cloudkitclient

import java.util.*

actual val client = CKClient(
    container = System.getenv("CONTAINER") ?: error("no container as secret passed"),
    keyID = System.getenv("KEYID") ?: error("no keyID as secret passed"),
    privateECPrime256v1Key = Base64.getMimeDecoder()
        .decode(System.getenv("PRIVATEKEY") ?: error("no private key as secret passed"))
)
