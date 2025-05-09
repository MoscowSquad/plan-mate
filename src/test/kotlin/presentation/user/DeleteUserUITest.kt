package presentation.user

import io.mockk.*
import logic.models.UserRole
import logic.usecases.user.DeleteUserUseCase
import logic.util.UnauthorizedAccessException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class DeleteUserUITest {

    private lateinit var deleteUserUseCase: DeleteUserUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var currentUserRole: () -> UserRole
    private lateinit var deleteUserUI: DeleteUserUI
    private val validUUID = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        deleteUserUseCase = mockk()
        consoleIO = mockk()
        currentUserRole = mockk()
        deleteUserUI = DeleteUserUI(deleteUserUseCase, currentUserRole, consoleIO)
    }

    @Test
    fun `when user is not admin, should show error message`() {
        // Given
        every { currentUserRole() } returns UserRole.MATE
        every { consoleIO.write(any()) } just runs

        // When
        deleteUserUI()

        // Then
        verify { consoleIO.write("\nError: Only ADMIN users can delete accounts.") }
        verify(exactly = 0) { consoleIO.read() }
    }

    @Test
    fun `when user is admin and provides valid UUID, should delete user successfully`() {
        // Given
        every { currentUserRole() } returns UserRole.ADMIN
        every { consoleIO.write(any()) } just runs
        every { consoleIO.read() } returns validUUID.toString()
        every { deleteUserUseCase(UserRole.ADMIN, validUUID) } just runs

        // When
        deleteUserUI()

        // Then
        verifySequence {
            consoleIO.write("\n=======================")
            consoleIO.write("║      DELETE USER     ║")
            consoleIO.write("=======================\n")
            consoleIO.write("Enter user ID to delete:")
            consoleIO.read()
            deleteUserUseCase(UserRole.ADMIN, validUUID)
            consoleIO.write("User with ID $validUUID has been successfully deleted.")
        }
    }

    @Test
    fun `when invalid UUID is provided, should show error message`() {
        // Given
        val invalidUUID = "not-a-valid-uuid"
        every { currentUserRole() } returns UserRole.ADMIN
        every { consoleIO.write(any()) } just runs
        every { consoleIO.read() } returns invalidUUID

        // When
        deleteUserUI()

        // Then
        verify { consoleIO.write("Error: Invalid UUID format. Please provide a valid user ID.") }
        verify(exactly = 0) { deleteUserUseCase(any(), any()) }
    }

    @Test
    fun `when unauthorized access occurs, should show appropriate error message`() {
        // Given
        every { currentUserRole() } returns UserRole.ADMIN
        every { consoleIO.write(any()) } just runs
        every { consoleIO.read() } returns validUUID.toString()
        every { deleteUserUseCase(UserRole.ADMIN, validUUID) } throws UnauthorizedAccessException("No permission")

        // When
        deleteUserUI()

        // Then
        verify { consoleIO.write("Error: You don't have permission to delete users.") }
    }

    @Test
    fun `when user not found, should show user not found error message`() {
        // Given
        every { currentUserRole() } returns UserRole.ADMIN
        every { consoleIO.write(any()) } just runs
        every { consoleIO.read() } returns validUUID.toString()
        every { deleteUserUseCase(UserRole.ADMIN, validUUID) } throws NoSuchElementException("User not found")

        // When
        deleteUserUI()

        // Then
        verify { consoleIO.write("Error: User with ID $validUUID not found.") }
    }

    @Test
    fun `when unexpected exception occurs, should show generic error message`() {
        // Given
        every { currentUserRole() } returns UserRole.ADMIN
        every { consoleIO.write(any()) } just runs
        every { consoleIO.read() } returns validUUID.toString()
        every { deleteUserUseCase(UserRole.ADMIN, validUUID) } throws RuntimeException("Something went wrong")

        // When
        deleteUserUI()

        // Then
        verify { consoleIO.write("Error: An unexpected error occurred while deleting the user.") }
    }
}