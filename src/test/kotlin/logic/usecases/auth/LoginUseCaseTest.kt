package logic.usecases.auth

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.User
import logic.models.UserRole
import logic.repositories.AuthenticationRepository
import logic.util.UserNotFoundException
import logic.util.toMD5Hash
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LoginUseCaseTest {

    private lateinit var loginUseCase: LoginUseCase
    private lateinit var authRepository: AuthenticationRepository

    private val validUser = User(
        id = UUID.randomUUID(),
        name = "validUser",
        hashedPassword = "correctPassword".toMD5Hash(),
        role = UserRole.ADMIN,
        projectIds = emptyList()
    )

    @BeforeEach
    fun setUp() {
        authRepository = mockk()
        loginUseCase = LoginUseCase(authRepository)

        every { authRepository.login("validUser", "correctPassword".toMD5Hash()) } returns validUser
        every { authRepository.login("VALIDUSER", "correctPassword".toMD5Hash()) } returns validUser
        every { authRepository.login("validUser", "wrongPassword".toMD5Hash()) } throws UserNotFoundException("validUser")
        every { authRepository.login("nonExistentUser", any()) } throws UserNotFoundException("nonExistentUser")
        every { authRepository.login("testUser", any()) } throws UserNotFoundException("testUser")
    }

    @Test
    fun `should return true for successful authentication`() {
        val result = loginUseCase("validUser", "correctPassword")
        assertNotNull(result)
        assertEquals("validUser", result.name)
    }

    @Test
    fun `should throw UserNotFoundException for failed authentication`() {
        assertThrows<UserNotFoundException> {
            loginUseCase("validUser", "wrongPassword")
        }
    }

    @Test
    fun `should throw when username is blank`() {
        val exception = assertThrows<IllegalArgumentException> {
            loginUseCase("", "password123")
        }
        assertEquals("Username cannot be blank", exception.message)
    }

    @Test
    fun `should throw when password is blank`() {
        val exception = assertThrows<IllegalArgumentException> {
            loginUseCase("username", "")
        }
        assertEquals("Password cannot be blank", exception.message)
    }

    @Test
    fun `should throw when password is too short`() {
        val exception = assertThrows<IllegalArgumentException> {
            loginUseCase("username", "short")
        }
        assertEquals("Password must be at least 8 characters", exception.message)
    }

    @Test
    fun `should ignore case for username comparison`() {
        val result = loginUseCase("VALIDUSER", "correctPassword")
        assertNotNull(result)
        assertEquals("validUser", result.name)
    }

    @Test
    fun `should throw when users file does not exist`() {
        every { authRepository.login(any(), any()) } throws IllegalStateException("users.csv not found")

        val exception = assertThrows<IllegalStateException> {
            loginUseCase("validUser", "correctPassword")
        }
        assertEquals("users.csv not found", exception.message)
    }

    @Test
    fun `should throw UserNotFoundException when user does not exist`() {
        val exception = assertThrows<UserNotFoundException> {
            loginUseCase("nonExistentUser", "anyPassword12")
        }
        assertEquals("User 'nonExistentUser' does not exist", exception.message)
    }

    @Test
    fun `should handle malformed CSV lines without enough columns`() {
        val exception = assertThrows<UserNotFoundException> {
            loginUseCase("testUser", "password123")
        }
        assertEquals("User 'testUser' does not exist", exception.message)

        verify { authRepository.login("testUser", "password123".toMD5Hash()) }
    }
}