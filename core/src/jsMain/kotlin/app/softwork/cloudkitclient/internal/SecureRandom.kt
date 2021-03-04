package app.softwork.cloudkitclient.internal

import kotlinx.browser.*
import org.khronos.webgl.*
import org.w3c.dom.*
import kotlin.random.*

internal external interface Crypto {
    fun getRandomValues(array: Uint32Array): Uint32Array
    val subtle: SubtleCrypto
}

internal external interface SubtleCrypto {
    suspend fun digest(algorithm: String, data: ArrayBuffer): ArrayBuffer
}

internal inline val Window.crypto: Crypto
    get() = asDynamic().crypto.unsafeCast<Crypto>()

internal actual val secureRandom = object : Random() {
    override fun nextBits(bitCount: Int): Int =
        nextInt().takeUpperBits(bitCount)

    override fun nextInt(): Int = window.crypto.getRandomValues(Uint32Array(1))[0]

    /**
     * Copied from [stdLib][kotlin.random.takeUpperBits]
     */
    private fun Int.takeUpperBits(bitCount: Int): Int =
        this.ushr(32 - bitCount) and (-bitCount).shr(31)
}