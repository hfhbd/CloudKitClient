package app.softwork.cloudkitclient.internal

import java.security.KeyFactory
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
internal suspend fun ecdsa(key: ByteArray, data: String): String =
    suspendCoroutine { cont ->
        val keySpec = PKCS8EncodedKeySpec(key, "EC")
        val signer = Signature.getInstance("SHA256withECDSA")
        val privateKey = KeyFactory.getInstance("EC").generatePrivate(keySpec)
        signer.initSign(privateKey)

        signer.update(data.toByteArray())
        val signed = signer.sign()
        val signedString = Base64.encode(signed)

        cont.resume(signedString)
    }
