package presentation.auth

import domain.usecases.auth.LoginUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO

class LoginUserUITest {
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var loginUserUI: LoginUserUI

    @BeforeEach
    fun setUp() {
        loginUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        loginUserUI = LoginUserUI(loginUseCase, consoleIO)
    }

    @Test
    fun `should login successfully on first attempt`() = runTest {
        // Given
        val username = "testUser"
        val password = "testPassword"

        coEvery { consoleIO.read() } returnsMany listOf(username, password)
        coEvery { loginUseCase(username, password) } returns mockk()
        coEvery { consoleIO.write(any()) } just runs

        // When
        loginUserUI()

        // Then
        coVerifySequence {
            consoleIO.write("🔐 Login")
            consoleIO.write("Enter username: ")
            consoleIO.read()
            consoleIO.write("Enter password: 🔑")
            consoleIO.read()
            loginUseCase(username, password)
            consoleIO.write("✅ Logged in successfully! Welcome back, $username.")
        }
    }

    @Test
    fun `should retry login after failure and then succeed`() = runTest {
        // Given
        val failedUsername = "invalidUser"
        val failedPassword = "invalidPass"
        val successUsername = "validUser"
        val successPassword = "validPass"
        val errorMessage = "Invalid credentials"

        coEvery { consoleIO.read() } returnsMany listOf(
            failedUsername, failedPassword,
            successUsername, successPassword
        )
        coEvery { loginUseCase(failedUsername, failedPassword) } throws Exception(errorMessage)
        coEvery { loginUseCase(successUsername, successPassword) } returns mockk()
        coEvery { consoleIO.write(any()) } just runs

        // When
        loginUserUI()

        // Then
        coVerifySequence {
            consoleIO.write("🔐 Login")
            consoleIO.write("Enter username: ")
            consoleIO.read()
            consoleIO.write("Enter password: 🔑")
            consoleIO.read()
            loginUseCase(failedUsername, failedPassword)
            consoleIO.write("Login failed. $errorMessage 😞")
            consoleIO.write("Enter username: ")
            consoleIO.read()
            consoleIO.write("Enter password: 🔑")
            consoleIO.read()
            loginUseCase(successUsername, successPassword)
            consoleIO.write("✅ Logged in successfully! Welcome back, $successUsername.")
        }

        coVerify {
            loginUseCase(failedUsername, failedPassword)
            loginUseCase(successUsername, successPassword)
            consoleIO.write("Login failed. $errorMessage 😞")
            consoleIO.write("✅ Logged in successfully! Welcome back, $successUsername.")
        }
    }

    @Test
    fun `should handle multiple login failures before success`() = runTest {
        // Given
        val inputs = listOf(
            "user1", "pass1",
            "user2", "pass2",
            "user3", "pass3"
        )
        val errors = listOf("Invalid username", "Password incorrect")

        coEvery { consoleIO.read() } returnsMany inputs
        coEvery { loginUseCase("user1", "pass1") } throws Exception(errors[0])
        coEvery { loginUseCase("user2", "pass2") } throws Exception(errors[1])
        coEvery { loginUseCase("user3", "pass3") } returns mockk()
        coEvery { consoleIO.write(any()) } just runs

        // When
        loginUserUI()

        // Then
        coVerifySequence {
            consoleIO.write("🔐 Login")

            consoleIO.write("Enter username: ")
            consoleIO.read()
            consoleIO.write("Enter password: 🔑")
            consoleIO.read()
            loginUseCase("user1", "pass1")
            consoleIO.write("Login failed. ${errors[0]} 😞")

            consoleIO.write("Enter username: ")
            consoleIO.read()
            consoleIO.write("Enter password: 🔑")
            consoleIO.read()
            loginUseCase("user2", "pass2")
            consoleIO.write("Login failed. ${errors[1]} 😞")

            consoleIO.write("Enter username: ")
            consoleIO.read()
            consoleIO.write("Enter password: 🔑")
            consoleIO.read()
            loginUseCase("user3", "pass3")
            consoleIO.write("✅ Logged in successfully! Welcome back, user3.")
        }
    }
}