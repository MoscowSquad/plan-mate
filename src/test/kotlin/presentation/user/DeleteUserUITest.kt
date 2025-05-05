package presentation.user

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import logic.models.UserRole
import logic.usecases.user.DeleteUserUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class DeleteUserUITest {
    private lateinit var deleteUserUseCase: DeleteUserUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var deleteUserUI: DeleteUserUI
    private val currentUserRole = mockk<() -> UserRole>()
    private val validUuidString = "123e4567-e89b-12d3-a456-426614174000"
    private val validUserId = UUID.fromString(validUuidString)
    private val adminRole = UserRole.ADMIN

    @BeforeEach
    fun setUp() {
        deleteUserUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        every { currentUserRole() } returns adminRole
        deleteUserUI = DeleteUserUI(deleteUserUseCase, currentUserRole, consoleIO)
    }

    @Test
    fun `should delete user successfully with valid UUID`() {
        // Given
        every { consoleIO.read() } returns validUuidString

        // When
        deleteUserUI.invoke()

        // Then
        verifySequence {
            consoleIO.write("\n=======================")
            consoleIO.write("║      DELETE USER     ║")
            consoleIO.write("=======================\n")
            consoleIO.write("Enter user ID to delete:")
            consoleIO.read()
            currentUserRole()
            deleteUserUseCase(adminRole, validUserId)
            consoleIO.write(" User with ID $validUserId has been successfully deleted.")
        }
    }

    @Test
    fun `should handle invalid UUID format`() {
        // Given
        val invalidUuid = "not-a-uuid"
        every { consoleIO.read() } returns invalidUuid

        // When
        deleteUserUI.invoke()

        // Then
        verifySequence {
            consoleIO.write("\n=======================")
            consoleIO.write("║      DELETE USER     ║")
            consoleIO.write("=======================\n")
            consoleIO.write("Enter user ID to delete:")
            consoleIO.read()
            consoleIO.write(" Invalid UUID format. Please provide a valid user ID.")
        }

        verify(exactly = 0) {
            deleteUserUseCase(any(), any())
        }
    }

    @Test
    fun `should use MATE role when currentUserRole returns MATE`() {
        // Given
        val mateRole = UserRole.MATE
        every { currentUserRole() } returns mateRole
        every { consoleIO.read() } returns validUuidString

        // When
        deleteUserUI.invoke()

        // Then
        verify {
            deleteUserUseCase(mateRole, validUserId)
        }
    }

    @Test
    fun `should trim user input before parsing UUID`() {
        // Given
        val uuidWithSpaces = "  $validUuidString  "
        every { consoleIO.read() } returns uuidWithSpaces

        // When
        deleteUserUI.invoke()

        // Then
        verify {
            deleteUserUseCase(any(), validUserId)
        }
    }
}