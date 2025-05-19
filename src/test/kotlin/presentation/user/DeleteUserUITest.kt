package presentation.user

import data.mongodb_data.mappers.toUUID
import data.session_manager.SessionManager
import domain.models.User.UserRole
import domain.usecases.user.DeleteUserUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class DeleteUserUITest {
    private lateinit var deleteUserUseCase: DeleteUserUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var deleteUserUI: DeleteUserUI
    private val userId = UUID.randomUUID()
    private val userIdString = userId.toString()

    @BeforeEach
    fun setUp() {
        deleteUserUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        deleteUserUI = DeleteUserUI(deleteUserUseCase, consoleIO)

        mockkStatic(String::toUUID)
        every { userIdString.toUUID() } returns userId

        every { consoleIO.read() } returns userIdString

        mockkObject(SessionManager)
    }

    @Test
    fun `should delete user successfully with admin role`() = runTest {
        // Given
        every { SessionManager.getCurrentUserRole() } returns UserRole.ADMIN
        coEvery { deleteUserUseCase(UserRole.ADMIN, userId) } returns Unit

        // When
        deleteUserUI.invoke()

        // Then
        coVerifySequence {
            SessionManager.getCurrentUserRole()
            consoleIO.write("\n=======================")
            consoleIO.write("║      DELETE USER     ║")
            consoleIO.write("=======================\n")
            consoleIO.write("Enter user ID to delete: ")
            consoleIO.read()
            deleteUserUseCase(UserRole.ADMIN, userId)
            consoleIO.write("✅ User with ID $userId has been successfully deleted.")
        }
    }

    @Test
    fun `should delete user successfully with non-admin role`() = runTest {
        // Given
        every { SessionManager.getCurrentUserRole() } returns UserRole.MATE
        coEvery { deleteUserUseCase(UserRole.MATE, userId) } returns Unit

        // When
        deleteUserUI.invoke()

        // Then
        coVerifySequence {
            SessionManager.getCurrentUserRole()
            consoleIO.write("\n=======================")
            consoleIO.write("║      DELETE USER     ║")
            consoleIO.write("=======================\n")
            consoleIO.write("Enter user ID to delete: ")
            consoleIO.read()
            deleteUserUseCase(UserRole.MATE, userId)
            consoleIO.write("✅ User with ID $userId has been successfully deleted.")
        }
    }

    @Test
    fun `should handle invalid UUID input`() = runTest {
        // Given
        val invalidUuid = "not-a-uuid"
        every { SessionManager.getCurrentUserRole() } returns UserRole.ADMIN
        every { consoleIO.read() } returns invalidUuid
        every { invalidUuid.toUUID() } throws IllegalArgumentException("Invalid UUID format")

        // When
        deleteUserUI.invoke()

        // Then
        coVerifySequence {
            SessionManager.getCurrentUserRole()
            consoleIO.write("\n=======================")
            consoleIO.write("║      DELETE USER     ║")
            consoleIO.write("=======================\n")
            consoleIO.write("Enter user ID to delete: ")
            consoleIO.read()
            consoleIO.write("❌ Error: Invalid UUID format. Please provide a valid user ID.")
        }

        coVerify(exactly = 0) { deleteUserUseCase(any(), any()) }
    }

    @Test
    fun `should handle exception when deleting user`() = runTest {
        // Given
        val errorMessage = "User not found"
        every { SessionManager.getCurrentUserRole() } returns UserRole.ADMIN
        coEvery { deleteUserUseCase(UserRole.ADMIN, userId) } throws RuntimeException(errorMessage)

        // When
        deleteUserUI.invoke()

        // Then
        coVerifySequence {
            SessionManager.getCurrentUserRole()
            consoleIO.write("\n=======================")
            consoleIO.write("║      DELETE USER     ║")
            consoleIO.write("=======================\n")
            consoleIO.write("Enter user ID to delete: ")
            consoleIO.read()
            deleteUserUseCase(UserRole.ADMIN, userId)
            consoleIO.write("❌ Failed to delete user. $errorMessage")
        }
    }

    @Test
    fun `should handle null user role`() = runTest {
        // Given
        every { SessionManager.getCurrentUserRole() } returns UserRole.MATE
        coEvery { deleteUserUseCase(UserRole.MATE, userId) } returns Unit

        // When
        deleteUserUI.invoke()

        // Then
        coVerifySequence {
            SessionManager.getCurrentUserRole()
            consoleIO.write("\n=======================")
            consoleIO.write("║      DELETE USER     ║")
            consoleIO.write("=======================\n")
            consoleIO.write("Enter user ID to delete: ")
            consoleIO.read()
            deleteUserUseCase(UserRole.MATE, userId)
            consoleIO.write("✅ User with ID $userId has been successfully deleted.")
        }
    }
}