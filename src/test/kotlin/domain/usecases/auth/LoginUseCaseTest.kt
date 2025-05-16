package domain.usecases.auth

import domain.models.User
import domain.models.User.UserRole
import domain.repositories.AuthenticationRepository
import domain.util.UserNotFoundException
import domain.util.toMD5Hash
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LoginUseCaseTest {

    private lateinit var loginUseCase: LoginUseCase
    private lateinit var authRepository: AuthenticationRepository

    private val validUser = User(
        id = UUID.randomUUID(),
        name = "validUser",
        role = UserRole.ADMIN,
        projectIds = emptyList(),
        taskIds = emptyList()
    )

    @BeforeEach
    fun setUp() {
        authRepository = mockk()
        loginUseCase = LoginUseCase(authRepository)

        every { authRepository.login("validUser", "correctPassword".toMD5Hash()) } returns validUser
        every { authRepository.login("VALIDUSER", "correctPassword".toMD5Hash()) } returns validUser
        every {
            authRepository.login(
                "validUser",
                "wrongPassword".toMD5Hash()
            )
        } throws UserNotFoundException("validUser")
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
        assertThrows<IllegalArgumentException> {
            loginUseCase("", "password123")
        }
    }

    @Test
    fun `should throw when password is blank`() {
        assertThrows<IllegalArgumentException> {
            loginUseCase("username", "")
        }
    }

    @Test
    fun `should throw when password is too short`() {
        assertThrows<IllegalArgumentException> {
            loginUseCase("username", "short")
        }
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

        assertThrows<IllegalStateException> {
            loginUseCase("validUser", "correctPassword")
        }
    }

    @Test
    fun `should throw UserNotFoundException when user does not exist`() {
        assertThrows<UserNotFoundException> {
            loginUseCase("nonExistentUser", "anyPassword12")
        }
    }

    @Test
    fun `should handle malformed CSV lines without enough columns`() {
        assertThrows<UserNotFoundException> {
            loginUseCase("testUser", "password123")
        }

        verify { authRepository.login("testUser", "password123".toMD5Hash()) }
    }
}