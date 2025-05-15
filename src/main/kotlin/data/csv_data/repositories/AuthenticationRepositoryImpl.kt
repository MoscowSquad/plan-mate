package data.csv_data.repositories

import data.csv_data.datasource.UserDataSource
import data.csv_data.dto.UserDto
import data.csv_data.mappers.toDto
import data.csv_data.mappers.toUser
import data.session_manager.LoggedInUser
import data.session_manager.SessionManager
import logic.models.User
import logic.repositories.AuthenticationRepository
import logic.util.UserNotFoundException
import logic.util.toMD5Hash

class AuthenticationRepositoryImpl(
    private val dataSource: UserDataSource,
) : AuthenticationRepository {

    val users = mutableListOf<UserDto>()

    init {
        users.addAll(dataSource.fetch())
    }

    override fun register(user: User, hashedPassword: String): User {
        require(users.none { it.name == user.name }) { "Username already exists" }
        users.add(user.toDto(hashedPassword))
        dataSource.save(users)
        SessionManager.currentUser = LoggedInUser(user.id, user.name, user.role, user.projectIds)
        return user
    }

    override fun login(name: String, password: String): User {
        val hashedPassword = password.toMD5Hash()
        val userDto = users.find { it.name == name && it.hashedPassword == hashedPassword }
            ?: throw UserNotFoundException(name)
        val user = userDto.toUser()

        SessionManager.currentUser = LoggedInUser(user.id, user.name, user.role, user.projectIds)

        return user
    }
}