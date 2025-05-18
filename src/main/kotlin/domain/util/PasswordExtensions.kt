package domain.util

import java.security.MessageDigest

@OptIn(ExperimentalStdlibApi::class)
fun toMD5Hash(plainText: String): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(plainText.toByteArray())
    return digest.toHexString()
}

fun isValidPasswordFormat(password: String): Boolean {
    val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,}$")
    return passwordRegex.matches(password)
}

fun matchesMD5Hash(actual: String, expected: String): Boolean =
    actual == expected