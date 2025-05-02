package logic.usecases

import logic.models.User
import logic.models.UserRole
import logic.repositoies.AuthenticationRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.fail

class RegisterUseCaseTest {

    private class FakeAuthRepository : AuthenticationRepository {
        val registeredUsers = mutableListOf<User>()
        var shouldThrow = false

        override fun register(user: User): User {
            if (shouldThrow) throw IllegalStateException("Repository error")
            registeredUsers.add(user)
            return user
        }

        override fun login(name: String, password: String): Boolean = false
    }

    private val fakeRepository = FakeAuthRepository()
    private val passwordHasher = { plain: String -> "hashed_$plain" }
    private val registerUseCase = RegisterUseCase(fakeRepository, passwordHasher)

    @Test
    fun `should return non-null user when registration succeeds`() {
        val result = registerUseCase(
            name = "testuser",
            plainPassword = "validPassword123",
            role = UserRole.MATE
        )
        assertNotNull(result)
    }

    @Test
    fun `should handle password hashing failure`() {
        val failingHasher = { _: String -> throw RuntimeException("Hashing failed") }
        val failingRegisterUseCase = RegisterUseCase(fakeRepository, failingHasher)

        assertThrows<RuntimeException> {
            failingRegisterUseCase(
                name = "testuser",
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
                name = "testuser",
                plainPassword = "password123",
                role = UserRole.MATE
            )
        }
    }

    @Test
    fun `should add user to repository when registration succeeds`() {
        registerUseCase(
            name = "testuser",
            plainPassword = "validPassword123",
            role = UserRole.MATE
        )
        assertTrue { fakeRepository.registeredUsers.size == 1 }
    }

    @Test
    fun `should store correct username when registration succeeds`() {
        registerUseCase(
            name = "testuser",
            plainPassword = "validPassword123",
            role = UserRole.MATE
        )
        assertTrue { fakeRepository.registeredUsers[0].name == "testuser" }
    }

    @Test
    fun `should store hashed password when registration succeeds`() {
        registerUseCase(
            name = "testuser",
            plainPassword = "validPassword123",
            role = UserRole.MATE
        )
        assertTrue { fakeRepository.registeredUsers[0].hashedPassword == "hashed_validPassword123" }
    }

    @Test
    fun `should include single project ID when one is provided`() {
        val projectId = UUID.randomUUID()
        registerUseCase(
            name = "projectuser",
            plainPassword = "password123",
            role = UserRole.MATE,
            projectIds = listOf(projectId)
        )
        assertTrue { fakeRepository.registeredUsers[0].projectIds.size == 1 }
    }

    @Test
    fun `should store correct project ID when one is provided`() {
        val projectId = UUID.randomUUID()
        registerUseCase(
            name = "projectuser",
            plainPassword = "password123",
            role = UserRole.MATE,
            projectIds = listOf(projectId)
        )
        assertTrue { fakeRepository.registeredUsers[0].projectIds[0] == projectId }
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
        val exception = assertThrows<IllegalArgumentException> {
            registerUseCase(
                name = "",
                plainPassword = "password123",
                role = UserRole.MATE
            )
        }
        assertTrue { exception.message?.contains("Username cannot be blank") ?: false }
    }

    @Test
    fun `should generate valid UUID for new user`() {
        val result = registerUseCase(
            name = "newuser",
            plainPassword = "password123",
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
                name = "testuser",
                plainPassword = "validPassword123",
                role = UserRole.MATE
            )
        }
    }

    @Test
    fun `should store correct role for admin user`() {
        registerUseCase(
            name = "adminuser",
            plainPassword = "adminpass",
            role = UserRole.ADMIN
        )
        assertTrue { fakeRepository.registeredUsers[0].role == UserRole.ADMIN }
    }

    @Test
    fun `should store empty project list when none provided`() {
        registerUseCase(
            name = "noprojects",
            plainPassword = "test12345",
            role = UserRole.MATE
        )
        assertTrue { fakeRepository.registeredUsers[0].projectIds.isEmpty() }
    }

    @Test
    fun `should throw when password is blank`() {
        val exception = assertThrows<IllegalArgumentException> {
            registerUseCase(
                name = "testuser",
                plainPassword = "",
                role = UserRole.MATE
            )
        }
        assertTrue { exception.message?.contains("Password cannot be blank") ?: false }
    }

    @Test
    fun `should throw when password is shorter than 8 characters`() {
        val exception = assertThrows<IllegalArgumentException> {
            registerUseCase(
                name = "testuser",
                plainPassword = "short",
                role = UserRole.MATE
            )
        }
        assertTrue { exception.message?.contains("Password must be at least 8 characters") ?: false }
    }

}