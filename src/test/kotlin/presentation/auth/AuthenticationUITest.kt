package presentation.auth

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
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
    fun `should call registerAdminUI when option 1 is selected`() = runTest {
        // Given
        coEvery { consoleIO.read() } returns "1"

        // When
        authenticationUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write(any())
            consoleIO.read()
            registerAdminUI()
        }
    }

    @Test
    fun `should call loginUserUI when option 2 is selected`() = runTest {
        // Given
        coEvery { consoleIO.read() } returns "2"

        // When
        authenticationUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write(any())
            consoleIO.read()
            loginUserUI()
        }
    }

    @Test
    fun `should display error message when the user enter input out of range`() = runTest {
        // Given
        coEvery { consoleIO.read() } returns "4"

        // When - Execute with try/catch to handle the recursion
        try {
            authenticationUI.invoke()
        } catch (e: StackOverflowError) {
            // Expected due to recursion in the implementation
        }

        // Then
        coVerify {
            consoleIO.write(any()) // Initial prompt
            consoleIO.read() // Read "4"
            consoleIO.write("\nInvalid input. Please enter a number between 1 and 3.") // Error message
        }
    }

}
