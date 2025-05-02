package data.repositories

import logic.models.User
import logic.models.UserRole
import logic.repositoies.AuthenticationRepository
import utilities.toMD5Hash
import java.io.File
import java.util.*

class AuthenticationRepositoryImpl(
    private val usersFile: File
) : AuthenticationRepository {

    val users = mutableListOf<User>()

    init {
        if (usersFile.exists()) {
            loadUsersFromFile()
        } else {
            usersFile.createNewFile()
            createDefaultAdmin()
        }
    }

    override fun register(user: User): User {
        require(users.none { it.name == user.name }) { "Username already exists" }
        val userWithHashedPassword = user.copy(hashedPassword = user.hashedPassword.toMD5Hash())
        users.add(userWithHashedPassword)
        saveUsersToFile()
        return userWithHashedPassword
    }

    override fun login(name: String, password: String): Boolean {
        val hashedPassword = password.toMD5Hash()
        return users.any { it.name == name && it.hashedPassword == hashedPassword }
    }

    fun loadUsersFromFile() {
        usersFile.readLines()
            .drop(1) // Skip header
            .forEach { line ->
                try {
                    val parts = line.split(",")
                    if (parts.size >= 5) {
                        users.add(
                            User(
                                id = UUID.fromString(parts[0]),
                                name = parts[1],
                                hashedPassword = parts[2],
                                role = UserRole.valueOf(parts[3]),
                                projectIds = parts[4].removeSurrounding("[", "]")
                                    .split(";")
                                    .filter { it.isNotBlank() }
                                    .map { UUID.fromString(it) }
                            )
                        )
                    }
                } catch (e: Exception) {
                    println("Error parsing user line: $line. Error: ${e.message}")
                }
            }
    }

    private fun saveUsersToFile() {
        val lines = mutableListOf("id,name,hashedPassword,role,projectIds")
        users.forEach { user ->
            lines.add(
                listOf(
                    user.id.toString(),
                    user.name,
                    user.hashedPassword,
                    user.role.name,
                    user.projectIds.joinToString(";") { it.toString() }
                ).joinToString(",")
            )
        }
        usersFile.writeText(lines.joinToString("\n"))
    }

    fun createDefaultAdmin() {
        if (users.none { it.role == UserRole.ADMIN }) {
            register(
                User(
                    id = UUID.randomUUID(),
                    name = "admin",
                    hashedPassword = "admin123", // This will be hashed in register()
                    role = UserRole.ADMIN,
                    projectIds = emptyList()
                )
            )
        }
    }
}