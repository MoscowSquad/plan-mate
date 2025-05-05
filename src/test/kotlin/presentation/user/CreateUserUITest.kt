package presentation.user

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import logic.models.UserRole
import logic.usecases.user.CreateUserUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO

class CreateUserUITest {
    private lateinit var createUserUseCase: CreateUserUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var createUserUI: CreateUserUI
    private val currentUserRole = UserRole.ADMIN
    private val username = "john"
    private val password = "password123"

    @BeforeEach
    fun setUp() {
        createUserUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        createUserUI = CreateUserUI(createUserUseCase, currentUserRole, consoleIO)

        every { consoleIO.read() } returnsMany listOf(username, password, "MATE")
    }

    @Test
    fun `should create user successfully`() {
        // Given
        every { createUserUseCase(any(), any()) } returns true

        // When
        createUserUI.invoke()

        // Then
        verifySequence {
            consoleIO.write("\n=== Create New User ===")
            consoleIO.write("Enter username:")
            consoleIO.read()
            consoleIO.write("Enter password:")
            consoleIO.read()
            consoleIO.write("Select role (ADMIN or MATE):")
            consoleIO.read()
            createUserUseCase(currentUserRole, any())
            consoleIO.write("User '$username' created successfully.")
        }
    }

    @Test
    fun `should handle failed user creation`() {
        // Given
        every { consoleIO.read() } returnsMany listOf(username, password, "ADMIN")
        every { createUserUseCase(any(), any()) } returns false

        // When
        createUserUI.invoke()

        // Then
        verifySequence {
            consoleIO.write("\n=== Create New User ===")
            consoleIO.write("Enter username:")
            consoleIO.read()
            consoleIO.write("Enter password:")
            consoleIO.read()
            consoleIO.write("Select role (ADMIN or MATE):")
            consoleIO.read()
            createUserUseCase(currentUserRole, any())
            consoleIO.write("Failed to create user. Username might already exist.")
        }
    }

    @Test
    fun `should handle invalid role input`() {
        // Given
        every { consoleIO.read() } returnsMany listOf(username, password, "INVALID_ROLE")

        // When
        createUserUI.invoke()

        // Then
        verifySequence {
            consoleIO.write("\n=== Create New User ===")
            consoleIO.write("Enter username:")
            consoleIO.read()
            consoleIO.write("Enter password:")
            consoleIO.read()
            consoleIO.write("Select role (ADMIN or MATE):")
            consoleIO.read()
            consoleIO.write("Invalid role. Please enter 'ADMIN' or 'MATE'.")
        }

        verify(exactly = 0) {
            createUserUseCase(any(), any())
        }
    }
}