package domain.usecases.auth

import data.csv_data.dto.UserDto
import data.csv_data.mappers.toDto
import data.csv_data.mappers.toUser
import domain.models.User
import domain.models.User.UserRole
import domain.repositories.AuthenticationRepository
import domain.util.toMD5Hash
import kotlinx.coroutines.test.runTest
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
    fun `should return non-null user when registration succeeds`(): Unit = runTest {
        // When
        val result = registerUseCase(
            name = "test user",
            plainPassword = "Valid@Password123",
            role = UserRole.MATE
        )

        // Then
        assertNotNull(result)
    }

    @Test
    fun `should handle password hashing failure`(): Unit = runTest {
        // Given
        val failingAuthRepository = object : AuthenticationRepository {
            override suspend fun register(user: User, hashedPassword: String): User {
                throw RuntimeException("Password hashing failed")
            }

            override suspend fun login(name: String, password: String): User {
                throw RuntimeException("Password hashing failed")
            }
        }

        val failingRegisterUseCase = RegisterUseCase(failingAuthRepository)

        // When & Then
        assertThrows<RuntimeException> {
            failingRegisterUseCase(
                name = "test user",
                plainPassword = "Valid@Password123",
                role = UserRole.MATE
            )
        }
    }

    @Test
    fun `should handle repository registration failure`(): Unit = runTest {
        // Given
        fakeRepository.shouldThrow = true

        // When & Then
        assertThrows<IllegalStateException> {
            registerUseCase(
                name = "test user",
                plainPassword = "Valid@Password123",
                role = UserRole.MATE
            )
        }
    }

    @Test
    fun `should add user to repository when registration succeeds`() = runTest {
        // When
        registerUseCase(
            name = "test user",
            plainPassword = "Valid@Password123",
            role = UserRole.MATE
        )

        // Then
        assertTrue { fakeRepository.registeredUsers.size == 1 }
    }

    @Test
    fun `should store correct username when registration succeeds`() = runTest {
        // When
        registerUseCase(
            name = "test user",
            plainPassword = "Valid@Password123",
            role = UserRole.MATE
        )

        // Then
        assertTrue { fakeRepository.registeredUsers[0].name == "test user" }
    }

    @Test
    fun `should store hashed password when registration succeeds`() = runTest {
        // Given
        val password = "Valid@Password123"

        // When
        registerUseCase(
            name = "test user",
            plainPassword = password,
            role = UserRole.MATE
        )

        // Then
        assertEquals(toMD5Hash(password), fakeRepository.registeredUsers[0].hashedPassword)
    }

    @Test
    fun `should include single project ID when one is provided`() = runTest {
        // Given
        val projectId = UUID.randomUUID()

        // When
        registerUseCase(
            name = "project user",
            plainPassword = "Valid@Password123",
            role = UserRole.MATE,
            projectIds = listOf(projectId)
        )

        // Then
        assertTrue { fakeRepository.registeredUsers[0].projectIds.size == 1 }
    }

    @Test
    fun `should store correct project ID when one is provided`() = runTest {
        // Given
        val projectId = UUID.randomUUID()

        // When
        registerUseCase(
            name = "project user",
            plainPassword = "Valid@Password123",
            role = UserRole.MATE,
            projectIds = listOf(projectId)
        )

        // Then
        assertTrue { fakeRepository.registeredUsers[0].toUser().projectIds[0] == projectId }
    }

    @Test
    fun `should throw when username is blank`(): Unit = runTest {
        // When & Then
        assertThrows<IllegalArgumentException> {
            registerUseCase(
                name = "",
                plainPassword = "Valid@Password123",
                role = UserRole.MATE
            )
        }
    }

    @Test
    fun `should throw with correct message when username is blank`() = runTest {
        // When & Then
        val exception = assertThrows<IllegalArgumentException> {
            registerUseCase(
                name = "",
                plainPassword = "Valid@Password123",
                role = UserRole.MATE
            )
        }

        assertEquals("Username cannot be blank", exception.message)
    }

    @Test
    fun `should generate valid UUID for new user`() = runTest {
        // When
        val result = registerUseCase(
            name = "new user",
            plainPassword = "Valid@Password123",
            role = UserRole.ADMIN
        )

        // Then
        try {
            UUID.fromString(result.id.toString())
            assertTrue { true }
        } catch (e: IllegalArgumentException) {
            fail("Generated ID is not a valid UUID")
        }
    }

    @Test
    fun `should propagate repository exceptions`(): Unit = runTest {
        // Given
        fakeRepository.shouldThrow = true

        // When & Then
        assertThrows<IllegalStateException> {
            registerUseCase(
                name = "test user",
                plainPassword = "Valid@Password123",
                role = UserRole.MATE
            )
        }
    }

    @Test
    fun `should store correct role for admin user`() = runTest {
        // When
        registerUseCase(
            name = "admin user",
            plainPassword = "Valid@Password123",
            role = UserRole.ADMIN
        )

        // Then
        assertTrue { fakeRepository.registeredUsers[0].toUser().role == UserRole.ADMIN }
    }

    @Test
    fun `should store empty project list when none provided`() = runTest {
        // When
        registerUseCase(
            name = "no projects",
            plainPassword = "Valid@Password123",
            role = UserRole.MATE
        )

        // Then
        assertTrue { fakeRepository.registeredUsers[0].projectIds.isEmpty() }
    }

    @Test
    fun `should throw when password is blank`() = runTest {
        // When & Then
        val exception = assertThrows<IllegalArgumentException> {
            registerUseCase(
                name = "test user",
                plainPassword = "",
                role = UserRole.MATE
            )
        }

        assertEquals("Password cannot be blank", exception.message)
    }

    @Test
    fun `should throw when password format is invalid`() = runTest {
        // When & Then
        val exception = assertThrows<IllegalArgumentException> {
            registerUseCase(
                name = "test user",
                plainPassword = "invalid password",
                role = UserRole.MATE
            )
        }

        assertEquals(
            "Password must be at least 8 characters long and contain at least one digit, one uppercase letter, and one lowercase letter",
            exception.message
        )
    }

    @Test
    fun `should store task IDs when provided`() = runTest {
        // Given
        val taskId = UUID.randomUUID()

        // When
        registerUseCase(
            name = "task user",
            plainPassword = "Valid@Password123",
            role = UserRole.MATE,
            taskIds = listOf(taskId)
        )

        // Then
        assertTrue { fakeRepository.registeredUsers[0].toUser().taskIds.isNotEmpty() }
        assertEquals(taskId, fakeRepository.registeredUsers[0].toUser().taskIds[0])
    }

    @Test
    fun `should store both project and task IDs when provided`() = runTest {
        // Given
        val projectId = UUID.randomUUID()
        val taskId = UUID.randomUUID()

        // When
        registerUseCase(
            name = "complete user",
            plainPassword = "Valid@Password123",
            role = UserRole.MATE,
            projectIds = listOf(projectId),
            taskIds = listOf(taskId)
        )

        // Then
        val user = fakeRepository.registeredUsers[0].toUser()
        assertEquals(projectId, user.projectIds[0])
        assertEquals(taskId, user.taskIds[0])
    }
}