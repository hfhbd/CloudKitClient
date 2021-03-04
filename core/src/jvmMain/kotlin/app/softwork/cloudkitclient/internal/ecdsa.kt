package app.softwork.cloudkitclient.internal

import java.security.KeyFactory
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal actual suspend fun ecdsa(key: ByteArray, data: String): String =
    suspendCoroutine { cont ->
        val signer = Signature.getInstance("ECDSA")
        val privateKey = KeyFactory.getInstance("ECDSA").generatePrivate(PKCS8EncodedKeySpec(key, "ECDSA"))
        signer.initSign(privateKey)
        signer.update(data.toByteArray())
        val signed = signer.sign().toString()
        cont.resume(signed)
    }