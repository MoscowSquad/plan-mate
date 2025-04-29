package utilities

import java.security.MessageDigest

@OptIn(ExperimentalStdlibApi::class)
fun String.toMD5Hash(): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(this.toByteArray())
    return digest.toHexString()
}

fun String.isValidPasswordFormat(): Boolean {
    val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,}$")
    return passwordRegex.matches(this)
}

fun String.matchesMD5Hash(expected: String): Boolean =
    this == expected