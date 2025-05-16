package domain.usecases.auth

import data.csv_data.dto.UserDto
import data.csv_data.mappers.toDto
import data.csv_data.mappers.toUser
import domain.models.User
import domain.models.User.UserRole
import domain.repositories.AuthenticationRepository
import domain.util.toMD5Hash
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.fail

class RegisterUseCaseTest {

    private class FakeAuthRepository : AuthenticationRepository {
        val registeredUsers = mutableListOf<UserDto>()
        var shouldThrow = false

        override suspend fun register(user: User, hashedPassword: String): User {
            if (shouldThrow) throw IllegalStateException("Repository error")
            registeredUsers.add(user.toDto(hashedPassword))
            return user
        }

        override suspend fun login(name: String, password: String): User {
            val user = registeredUsers.find { it.name == name && it.hashedPassword == password }
                ?: throw IllegalArgumentException("Invalid credentials")
            return user.toUser()
        }
    }

    private val fakeRepository = FakeAuthRepository()
    private val registerUseCase = RegisterUseCase(fakeRepository)

    @Test
    fun `should return non-null user when registration succeeds`() {
        val result = registerUseCase(
            name = "test user",
            plainPassword = "valid@Password123",
            role = UserRole.MATE
        )
        assertNotNull(result)
    }

    @Test
    fun `should handle password hashing failure`() {
        val failingAuthRepository = object : AuthenticationRepository {
            override suspend fun register(user: User, hashedPassword: String): User {
                throw RuntimeException("Password hashing failed")
            }

            override suspend fun login(name: String, password: String): User {
                throw RuntimeException("Password hashing failed")
            }
        }

        val failingRegisterUseCase = RegisterUseCase(failingAuthRepository)

        assertThrows<RuntimeException> {
            failingRegisterUseCase(
                name = "test user",
                plainPassword = "password123",
                role = UserRole.MATE
            )
        }
    }

    @Test
    fun `should handle repository registration failure`() {
        fakeRepository.shouldThrow = true

        assertThrows<IllegalStateException> {
            registerUseCase(
                name = "test user",
                plainPassword = "valid@Password123",
                role = UserRole.MATE
            )
        }
    }

    @Test
    fun `should add user to repository when registration succeeds`() {
        registerUseCase(
            name = "test user",
            plainPassword = "valid@Password123",
            role = UserRole.MATE
        )
        assertTrue { fakeRepository.registeredUsers.size == 1 }
    }

    @Test
    fun `should store correct username when registration succeeds`() {
        registerUseCase(
            name = "test user",
            plainPassword = "valid@Password123",
            role = UserRole.MATE
        )
        assertTrue { fakeRepository.registeredUsers[0].name == "test user" }
    }

    @Test
    fun `should store hashed password when registration succeeds`() {
        val password = "valid@Password123"

        registerUseCase(
            name = "test user",
            plainPassword = password,
            role = UserRole.MATE
        )
        assertEquals(password.toMD5Hash(), fakeRepository.registeredUsers[0].hashedPassword)
    }

    @Test
    fun `should include single project ID when one is provided`() {
        val projectId = UUID.randomUUID()
        registerUseCase(
            name = "project user",
            plainPassword = "valid@Password123",
            role = UserRole.MATE,
            projectIds = listOf(projectId)
        )
        assertTrue { fakeRepository.registeredUsers[0].projectIds.size == 1 }
    }

    @Test
    fun `should store correct project ID when one is provided`() {
        val projectId = UUID.randomUUID()
        registerUseCase(
            name = "project user",
            plainPassword = "valid@Password123",
            role = UserRole.MATE,
            projectIds = listOf(projectId)
        )
        assertTrue { fakeRepository.registeredUsers[0].toUser().projectIds[0] == projectId }
    }

    @Test
    fun `should throw when username is blank`() {
        assertThrows<IllegalArgumentException> {
            registerUseCase(
                name = "",
                plainPassword = "password123",
                role = UserRole.MATE
            )
        }
    }

    @Test
    fun `should throw with correct message when username is blank`() {
        assertThrows<IllegalArgumentException> {
            registerUseCase(
                name = "",
                plainPassword = "password123",
                role = UserRole.MATE
            )
        }
    }

    @Test
    fun `should generate valid UUID for new user`() {
        val result = registerUseCase(
            name = "new user",
            plainPassword = "valid@Password123",
            role = UserRole.ADMIN
        )
        try {
            UUID.fromString(result.id.toString())
            assertTrue { true }
        } catch (e: IllegalArgumentException) {
            fail("Generated ID is not a valid UUID")
        }
    }

    @Test
    fun `should propagate repository exceptions`() {
        fakeRepository.shouldThrow = true
        assertThrows<IllegalStateException> {
            registerUseCase(
                name = "test user",
                plainPassword = "valid@Password123",
                role = UserRole.MATE
            )
        }
    }

    @Test
    fun `should store correct role for admin user`() {
        registerUseCase(
            name = "admin user",
            plainPassword = "valid@Password123",
            role = UserRole.ADMIN
        )
        assertTrue { fakeRepository.registeredUsers[0].toUser().role == UserRole.ADMIN }
    }

    @Test
    fun `should store empty project list when none provided`() {
        registerUseCase(
            name = "no projects",
            plainPassword = "valid@Password123",
            role = UserRole.MATE
        )
        assertTrue { fakeRepository.registeredUsers[0].projectIds.isEmpty() }
    }

    @Test
    fun `should throw when password is blank`() {
        assertThrows<IllegalArgumentException> {
            registerUseCase(
                name = "test user",
                plainPassword = "",
                role = UserRole.MATE
            )
        }
    }

    @Test
    fun `should throw when password is shorter than 8 characters`() {
        assertThrows<IllegalArgumentException> {
            registerUseCase(
                name = "test user",
                plainPassword = "short",
                role = UserRole.MATE
            )
        }
    }
}