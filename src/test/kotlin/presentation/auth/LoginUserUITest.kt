package presentation.auth

import fake.FakeConsoleIO
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import logic.usecases.auth.LoginUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class LoginUserUITest {
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var consoleIO: FakeConsoleIO
    private lateinit var loginUserUI: LoginUserUI

    @BeforeEach
    fun setUp() {
        loginUseCase = mockk(relaxed = true)
        consoleIO = FakeConsoleIO(LinkedList(listOf("test user", "test password")))
        loginUserUI = LoginUserUI(loginUseCase, consoleIO)
    }

    @Test
    fun `should login successfully on first attempt`() {
        // Given
        every { loginUseCase("test user", "test password") } returns true

        // When
        loginUserUI.invoke()

        // Then
        verifySequence {
            loginUseCase("test user", "test password")
        }

        val outputs = listOf(
            "🔐 Login",
            "Enter username: ",
            "Enter password: 🔑",
            "✅ Logged in successfully! Welcome back, test user."
        )

        outputs.forEachIndexed { index, message ->
            assert(consoleIO.outputs[index] == message)
        }
    }

    @Test
    fun `should retry login after failure`() {
        // Given
        consoleIO = FakeConsoleIO(LinkedList(listOf("bad user", "bad password", "good user", "good password")))
        loginUserUI = LoginUserUI(loginUseCase, consoleIO)

        every { loginUseCase("bad user", "bad password") } throws Exception("Invalid credentials")
        every { loginUseCase("good user", "good password") } returns true

        // When
        loginUserUI.invoke()

        // Then
        verifySequence {
            loginUseCase("bad user", "bad password")
            loginUseCase("good user", "good password")
        }

        val outputs = listOf(
            "🔐 Login",
            "Enter username: ",
            "Enter password: 🔑",
            "Login failed. Invalid credentials 😞",
            "Enter username: ",
            "Enter password: 🔑",
            "✅ Logged in successfully! Welcome back, good user."
        )

        outputs.forEachIndexed { index, message ->
            assert(consoleIO.outputs[index] == message)
        }
    }

    @Test
    fun `should handle multiple failed login attempts`() {
        // Given
        consoleIO = FakeConsoleIO(
            LinkedList(
                listOf(
                    "user1", "pass1",
                    "user2", "pass2",
                    "user3", "pass3"
                )
            )
        )
        loginUserUI = LoginUserUI(loginUseCase, consoleIO)

        every { loginUseCase("user1", "pass1") } throws Exception("Account locked")
        every { loginUseCase("user2", "pass2") } throws Exception("Network error")
        every { loginUseCase("user3", "pass3") } returns true

        // When
        loginUserUI.invoke()

        // Then
        verify {
            loginUseCase("user1", "pass1")
            loginUseCase("user2", "pass2")
            loginUseCase("user3", "pass3")
        }

        assert(consoleIO.outputs.contains("Login failed. Account locked 😞"))
        assert(consoleIO.outputs.contains("Login failed. Network error 😞"))
        assert(consoleIO.outputs.contains("✅ Logged in successfully! Welcome back, user3."))
    }
}