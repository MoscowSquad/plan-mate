package presentation.user

import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import logic.usecases.user.GetUserByIdUseCase
import logic.models.User
import logic.models.UserRole
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class GetUserByIdUITest {
    private lateinit var getUserByIdUseCase: GetUserByIdUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var getUserByIdUI: GetUserByIdUI
    private val validUuidString = "123e4567-e89b-12d3-a456-426614174000"
    private val validUserId = UUID.fromString(validUuidString)

    @BeforeEach
    fun setUp() {
        getUserByIdUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        getUserByIdUI = GetUserByIdUI(getUserByIdUseCase, consoleIO)
    }

    @Test
    fun `should display user when valid UUID is entered`() {
        // Given
        val user = mockk<User>()
        every { user.name } returns "John Doe"
        every { user.role } returns UserRole.ADMIN
        every { consoleIO.read() } returns validUuidString
        every { getUserByIdUseCase(validUserId) } returns user

        // When
        getUserByIdUI.invoke()

        // Then
        verifySequence {
            consoleIO.write("\nEnter user ID:")
            consoleIO.read()
            getUserByIdUseCase(validUserId)
            consoleIO.write("Found: John Doe, Role: ADMIN")
        }
    }
    @Test
    fun `should display error message when invalid UUID is entered`() {
        // Given
        val invalidUuid = "not-a-uuid"
        every { consoleIO.read() } returns invalidUuid

        // When
        getUserByIdUI.invoke()

        // Then
        verifySequence {
            consoleIO.write("\nEnter user ID:")
            consoleIO.read()
            consoleIO.write("Invalid UUID")
        }
    }
}