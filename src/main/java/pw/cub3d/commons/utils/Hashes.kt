package pw.cub3d.commons.utils

import java.math.BigInteger
import java.security.MessageDigest


object Hashes {
    fun md5(s: String): String {
        val digest: MessageDigest = MessageDigest.getInstance("MD5")

        digest.update(s.toByteArray(), 0, s.length)
        val magnitude = digest.digest()
        val bi = BigInteger(1, magnitude)
        return String.format("%0" + (magnitude.size shl 1) + "x", bi)
    }
}