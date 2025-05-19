package presentation.user

import data.session_manager.SessionManager
import domain.models.User
import domain.models.User.UserRole
import domain.usecases.user.GetAllUsersUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class GetAllUserUITest {
    private lateinit var getAllUsersUseCase: GetAllUsersUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var getAllUserUI: GetAllUserUI

    @BeforeEach
    fun setUp() {
        getAllUsersUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        getAllUserUI = GetAllUserUI(getAllUsersUseCase, consoleIO)

        mockkObject(SessionManager)
    }

    @Test
    fun `should display users successfully when users exist`() = runTest {
        // Given
        val userId1 = UUID.randomUUID()
        val userId2 = UUID.randomUUID()
        val taskId1 = UUID.randomUUID()
        val taskId2 = UUID.randomUUID()
        val userList = listOf(
            User(userId1, "test user1", UserRole.ADMIN, emptyList(), listOf(taskId1)),
            User(userId2, "test user2", UserRole.MATE, emptyList(), listOf(taskId1, taskId2))
        )
        every { SessionManager.getCurrentUserRole() } returns UserRole.ADMIN
        coEvery { getAllUsersUseCase(UserRole.ADMIN) } returns userList

        // When
        getAllUserUI.invoke()

        // Then
        coVerify {
            SessionManager.getCurrentUserRole()
            getAllUsersUseCase(UserRole.ADMIN)
        }
        verifySequence {
            consoleIO.write("\n╔══════════════════════════════════════════════════════════╗")
            consoleIO.write("║                   USER DIRECTORY                         ║")
            consoleIO.write("╚══════════════════════════════════════════════════════════╝")
            consoleIO.write("\n┌───────┬───────────────┬───────┬───────────────────────────────────────┐")
            consoleIO.write("│ Index │ Username      │ Role  │ Assigned Tasks                        │")
            consoleIO.write("├───────┼───────────────┼───────┼───────────────────────────────────────┤")
            consoleIO.write(any()) // Row 1
            consoleIO.write(any()) // Row 2
            consoleIO.write("└───────┴───────────────┴───────┴───────────────────────────────────────┘")
            consoleIO.write("\nTotal registered users: 2\n")
        }
    }

    @Test
    fun `should display empty message when no users exist`() = runTest {
        // Given
        val emptyList = emptyList<User>()
        every { SessionManager.getCurrentUserRole() } returns UserRole.ADMIN
        coEvery { getAllUsersUseCase(UserRole.ADMIN) } returns emptyList

        // When
        getAllUserUI.invoke()

        // Then
        coVerify {
            SessionManager.getCurrentUserRole()
            getAllUsersUseCase(UserRole.ADMIN)
        }
        verifySequence {
            consoleIO.write("\n╔══════════════════════════════════════════════════════════╗")
            consoleIO.write("║                   USER DIRECTORY                         ║")
            consoleIO.write("╚══════════════════════════════════════════════════════════╝")
            consoleIO.write("\nℹ️  No users found in the system")
        }
    }

    @Test
    fun `should handle exception when fetching users fails`() = runTest {
        // Given
        val errorMessage = "Failed to connect to database"
        every { SessionManager.getCurrentUserRole() } returns UserRole.ADMIN
        coEvery { getAllUsersUseCase(UserRole.ADMIN) } throws RuntimeException(errorMessage)

        // When
        getAllUserUI.invoke()

        // Then
        coVerify {
            SessionManager.getCurrentUserRole()
            getAllUsersUseCase(UserRole.ADMIN)
        }
        verifySequence {
            consoleIO.write("\n╔══════════════════════════════════════════════════════════╗")
            consoleIO.write("║                   USER DIRECTORY                         ║")
            consoleIO.write("╚══════════════════════════════════════════════════════════╝")
            consoleIO.write("\n❌ Failed to load users: $errorMessage")
        }
    }

    @Test
    fun `should display users with different roles`() = runTest {
        // Given
        val userId1 = UUID.randomUUID()
        val userId2 = UUID.randomUUID()
        val userList = listOf(
            User(userId1, "test user1", UserRole.ADMIN, emptyList(), emptyList()),
            User(userId2, "test user2", UserRole.MATE, emptyList(), emptyList())
        )
        every { SessionManager.getCurrentUserRole() } returns UserRole.ADMIN
        coEvery { getAllUsersUseCase(UserRole.ADMIN) } returns userList

        // When
        getAllUserUI.invoke()

        // Then
        coVerify {
            SessionManager.getCurrentUserRole()
            getAllUsersUseCase(UserRole.ADMIN)
        }
        verify {
            consoleIO.write(match { it.contains("No tasks assigned") })
        }
    }

    @Test
    fun `should request users with non-admin role`() = runTest {
        // Given
        val userId = UUID.randomUUID()
        val userList = listOf(
            User(userId, "test user", UserRole.MATE, emptyList(), emptyList())
        )
        every { SessionManager.getCurrentUserRole() } returns UserRole.MATE
        coEvery { getAllUsersUseCase(UserRole.MATE) } returns userList

        // When
        getAllUserUI.invoke()

        // Then
        coVerify {
            SessionManager.getCurrentUserRole()
            getAllUsersUseCase(UserRole.MATE)
        }
    }
}