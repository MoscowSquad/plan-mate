package logic.usecases.auth

import logic.util.toMD5Hash
import logic.util.UserNotFoundException
import java.io.File

class LoginUseCase {

    private val usersFile = File("src/main/resources/users.csv")

    operator fun invoke(username: String, plainPassword: String): Boolean {
        require(username.isNotBlank()) { "Username cannot be blank" }
        require(plainPassword.isNotBlank()) { "Password cannot be blank" }

        val hashedPassword = plainPassword.toMD5Hash()

        if (!usersFile.exists()) {
            throw IllegalStateException("users.csv not found at: ${usersFile.absolutePath}")
        }

        usersFile.useLines { lines ->
            lines.drop(1).forEach { line ->
                val columns = line.split(",").map { it.trim() }
                if (columns.size >= 3) {
                    val csvUsername = columns[1]
                    val csvHashedPassword = columns[2]
                    if (csvUsername.equals(username, ignoreCase = true) && csvHashedPassword == hashedPassword) {
                        return true
                    }
                }
            }
        }

        throw UserNotFoundException(username)
    }
}
