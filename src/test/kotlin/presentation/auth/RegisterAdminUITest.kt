package presentation.auth

import domain.models.User
import domain.models.User.UserRole
import domain.usecases.auth.RegisterUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO

class RegisterAdminUITest {
    private lateinit var registerUseCase: RegisterUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var registerAdminUI: RegisterAdminUI

    @BeforeEach
    fun setUp() {
        registerUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        registerAdminUI = RegisterAdminUI(registerUseCase, consoleIO)
    }

    @Test
    fun `should register admin successfully on first attempt`() = runTest {
        // Given
        val username = "adminUser"
        val password = "P@ssw0rd123"
        val mockUser = mockk<User>(relaxed = true)

        coEvery { consoleIO.read() } returnsMany listOf(username, password)
        coEvery { registerUseCase(username, password, UserRole.ADMIN) } returns mockUser
        coEvery { consoleIO.write(any()) } just runs

        // When
        registerAdminUI()

        // Then
        coVerifySequence {
            consoleIO.write("🛡️ Register Admin")
            consoleIO.write("Enter admin username: ")
            consoleIO.read()
            consoleIO.write(any()) // Password requirements
            consoleIO.write("Enter password: 🔑")
            consoleIO.read()
            registerUseCase(username, password, UserRole.ADMIN)
            consoleIO.write("Admin registered successfully! 🎉")
            consoleIO.write("✅ Welcome, $username Let's begin by adding your first project.")
        }
    }

    @Test
    fun `should retry registration after failure`() = runTest {
        // Given
        val failedUsername = "existingAdmin"
        val failedPassword = "WeakPw1!"
        val successUsername = "newAdmin"
        val successPassword = "Str0ng#Pass2"
        val mockUser = mockk<User>(relaxed = true)
        val errorMessage = "Username already exists"

        coEvery { consoleIO.read() } returnsMany listOf(
            failedUsername,
            failedPassword,
            successUsername,
            successPassword
        )
        coEvery { registerUseCase(failedUsername, failedPassword, UserRole.ADMIN) } throws Exception(errorMessage)
        coEvery { registerUseCase(successUsername, successPassword, UserRole.ADMIN) } returns mockUser
        coEvery { consoleIO.write(any()) } just runs

        // When
        registerAdminUI()

        // Then
        coVerifySequence {
            consoleIO.write("🛡️ Register Admin")
            consoleIO.write("Enter admin username: ")
            consoleIO.read()
            consoleIO.write(any()) // Password requirements
            consoleIO.write("Enter password: 🔑")
            consoleIO.read()
            registerUseCase(failedUsername, failedPassword, UserRole.ADMIN)
            consoleIO.write("Registration failed. $errorMessage ❌")
            consoleIO.write("Enter admin username: ")
            consoleIO.read()
            consoleIO.write(any()) // Password requirements
            consoleIO.write("Enter password: 🔑")
            consoleIO.read()
            registerUseCase(successUsername, successPassword, UserRole.ADMIN)
            consoleIO.write("Admin registered successfully! 🎉")
            consoleIO.write("✅ Welcome, $successUsername Let's begin by adding your first project.")
        }

        coVerify {
            registerUseCase(failedUsername, failedPassword, UserRole.ADMIN)
            registerUseCase(successUsername, successPassword, UserRole.ADMIN)
            consoleIO.write("Registration failed. $errorMessage ❌")
            consoleIO.write("Admin registered successfully! 🎉")
            consoleIO.write("✅ Welcome, $successUsername Let's begin by adding your first project.")
        }
    }

    @Test
    fun `should handle multiple registration failures before success`() = runTest {
        // Given
        val inputs = listOf(
            "admin1", "weak1",
            "admin2", "short2",
            "admin3", "Valid@Pw3"
        )
        val errors = listOf("Password too weak", "Password too short")
        val mockUser = mockk<User>(relaxed = true)

        coEvery { consoleIO.read() } returnsMany inputs
        coEvery { registerUseCase("admin1", "weak1", UserRole.ADMIN) } throws Exception(errors[0])
        coEvery { registerUseCase("admin2", "short2", UserRole.ADMIN) } throws Exception(errors[1])
        coEvery { registerUseCase("admin3", "Valid@Pw3", UserRole.ADMIN) } returns mockUser
        coEvery { consoleIO.write(any()) } just runs

        // When
        registerAdminUI()

        // Then
        // Then
        coVerifySequence {
            consoleIO.write("🛡️ Register Admin")
            consoleIO.write("Enter admin username: ")
            consoleIO.read()
            // Match the actual multi-line password requirements format
            consoleIO.write(
                """# At least one lowercase letter
# At least one uppercase letter
# At least one digit
# At least one special character
# Minimum length of 8 characters"""
            )
            consoleIO.write("Enter password: 🔑")
            consoleIO.read()
            registerUseCase("admin1", "weak1", UserRole.ADMIN)
            consoleIO.write("Registration failed. ${errors[0]} ❌")

            consoleIO.write("Enter admin username: ")
            consoleIO.read()
            consoleIO.write(
                """# At least one lowercase letter
# At least one uppercase letter
# At least one digit
# At least one special character
# Minimum length of 8 characters"""
            )
            consoleIO.write("Enter password: 🔑")
            consoleIO.read()
            registerUseCase("admin2", "short2", UserRole.ADMIN)
            consoleIO.write("Registration failed. ${errors[1]} ❌")

            consoleIO.write("Enter admin username: ")
            consoleIO.read()
            consoleIO.write(
                """# At least one lowercase letter
# At least one uppercase letter
# At least one digit
# At least one special character
# Minimum length of 8 characters"""
            )
            consoleIO.write("Enter password: 🔑")
            consoleIO.read()
            registerUseCase("admin3", "Valid@Pw3", UserRole.ADMIN)
            consoleIO.write("Admin registered successfully! 🎉")
            consoleIO.write("✅ Welcome, admin3 Let's begin by adding your first project.")
        }
    }
}