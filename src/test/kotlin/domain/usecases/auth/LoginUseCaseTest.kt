package domain.usecases.auth

    import domain.models.User
    import domain.models.User.UserRole
    import domain.repositories.AuthenticationRepository
    import domain.util.UserNotFoundException
    import domain.util.toMD5Hash
    import io.mockk.coEvery
    import io.mockk.coVerify
    import io.mockk.mockk
    import kotlinx.coroutines.test.runTest
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

            coEvery { authRepository.login("validUser", toMD5Hash("correctPassword")) } returns validUser
            coEvery { authRepository.login("VALID USER", toMD5Hash("correctPassword")) } returns validUser
            coEvery {
                authRepository.login(
                    "validUser",
                    toMD5Hash("wrongPassword")
                )
            } throws UserNotFoundException()
            coEvery { authRepository.login("nonExistentUser", any()) } throws UserNotFoundException()
            coEvery { authRepository.login("testUser", any()) } throws UserNotFoundException()
        }

        @Test
        fun `invoke should return user for successful authentication`() = runTest {
            // Given

            // When
            val result = loginUseCase("validUser", "correctPassword")

            // Then
            assertNotNull(result)
            assertEquals("validUser", result.name)
            coVerify(exactly = 1) { authRepository.login("validUser", toMD5Hash("correctPassword")) }
        }

        @Test
        fun `invoke should throw UserNotFoundException for failed authentication`() = runTest {
            // Given

            // When & Then
            assertThrows<UserNotFoundException> {
                loginUseCase("validUser", "wrongPassword")
            }

            coVerify(exactly = 1) { authRepository.login("validUser", toMD5Hash("wrongPassword")) }
        }

        @Test
        fun `invoke should throw when username is blank`() = runTest {
            // When & Then
            assertThrows<IllegalArgumentException> {
                loginUseCase("", "password123")
            }

            coVerify(exactly = 0) { authRepository.login(any(), any()) }
        }

        @Test
        fun `invoke should throw when password is blank`() = runTest {
            // When & Then
            assertThrows<IllegalArgumentException> {
                loginUseCase("username", "")
            }

            coVerify(exactly = 0) { authRepository.login(any(), any()) }
        }

        @Test
        fun `invoke should throw when password is too short`() = runTest {
            // When & Then
            assertThrows<IllegalArgumentException> {
                loginUseCase("username", "short")
            }

            coVerify(exactly = 0) { authRepository.login(any(), any()) }
        }

        @Test
        fun `invoke should ignore case for username comparison`() = runTest {
            // Given

            // When
            val result = loginUseCase("VALID USER", "correctPassword")

            // Then
            assertNotNull(result)
            assertEquals("validUser", result.name)
            coVerify(exactly = 1) { authRepository.login("VALID USER", toMD5Hash("correctPassword")) }
        }

        @Test
        fun `invoke should throw when users file does not exist`() = runTest {
            // Given
            coEvery { authRepository.login(any(), any()) } throws IllegalStateException("users.csv not found")

            // When & Then
            assertThrows<IllegalStateException> {
                loginUseCase("validUser", "correctPassword")
            }

            coVerify(exactly = 1) { authRepository.login("validUser", toMD5Hash("correctPassword")) }
        }

        @Test
        fun `invoke should throw UserNotFoundException when user does not exist`() = runTest {
            // Given

            // When & Then
            assertThrows<UserNotFoundException> {
                loginUseCase("nonExistentUser", "anyPassword12")
            }

            coVerify(exactly = 1) { authRepository.login("nonExistentUser", toMD5Hash("anyPassword12")) }
        }

        @Test
        fun `invoke should handle malformed CSV lines without enough columns`() = runTest {
            // Given

            // When & Then
            assertThrows<UserNotFoundException> {
                loginUseCase("testUser", "password123")
            }

            coVerify(exactly = 1) { authRepository.login("testUser", toMD5Hash("password123")) }
        }
    }