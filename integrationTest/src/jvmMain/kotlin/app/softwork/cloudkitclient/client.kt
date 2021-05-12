package app.softwork.cloudkitclient

import java.util.*

actual fun client() = CKClient(
    container = System.getenv("container") ?: error("no container as secret passed"),
    keyID = System.getenv("keyID") ?: error("no keyID as secret passed"),
    privateECPrime256v1Key = Base64.getMimeDecoder()
        .decode(System.getenv("privateKey") ?: error("no private key as secret passed"))
)
