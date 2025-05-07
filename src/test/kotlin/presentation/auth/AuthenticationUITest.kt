package presentation.auth

import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO

class AuthenticationUITest {
    private lateinit var registerAdminUI: RegisterAdminUI
    private lateinit var loginUserUI: LoginUserUI
    private lateinit var consoleIO: ConsoleIO
    private lateinit var authenticationUI: AuthenticationUI

    @BeforeEach
    fun setUp() {
        registerAdminUI = mockk(relaxed = true)
        loginUserUI = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        authenticationUI = AuthenticationUI(registerAdminUI, loginUserUI, consoleIO)
    }

    @Test
    fun `should call registerAdminUI when option 1 is selected`() {
        // Given
        every { consoleIO.read() } returns "1"

        // When
        authenticationUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any())
            consoleIO.read()
            registerAdminUI()
        }
    }

    @Test
    fun `should call loginUserUI when option 2 is selected`() {
        // Given
        every { consoleIO.read() } returns "2"

        // When
        authenticationUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any())
            consoleIO.read()
            loginUserUI()
        }
    }

    @Test
    fun `should display error message when the user enter invalid input`() {
        // Given
        every { consoleIO.read() } returns "invalid"

        // When
        authenticationUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("\nInvalid input. Please enter a number between 1 and 3.")
        }
    }

    @Test
    fun `should display error message when the user enter input out of range`() {
        // Given
        every { consoleIO.read() } returns "4"

        // When
        authenticationUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("\nInvalid input. Please enter a number between 1 and 3.")
        }
    }
}