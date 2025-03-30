package app.softwork.cloudkitclient.internal

import java.security.*
import java.security.spec.*
import kotlin.coroutines.*
import kotlin.io.encoding.*

@OptIn(ExperimentalEncodingApi::class)
internal actual suspend fun ecdsa(key: ByteArray, data: String): String =
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
