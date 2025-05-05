package presentation.user

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import logic.models.User
import logic.models.UserRole
import logic.usecases.user.GetAllUsersUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class GetAllUserUITest {
    private lateinit var getAllUsersUseCase: GetAllUsersUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var getAllUserUI: GetAllUserUI
    private val currentUserRole = mockk<() -> UserRole>()
    private val adminRole = UserRole.ADMIN
    private val userId1 = UUID.randomUUID()
    private val userId2 = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        getAllUsersUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        every { currentUserRole() } returns adminRole
        getAllUserUI = GetAllUserUI(getAllUsersUseCase, currentUserRole, consoleIO)
    }

    @Test
    fun `should display all users when users exist`() {
        // Given
        val user1 = User(userId1, "user1", "password1", UserRole.ADMIN, emptyList())
        val user2 = User(userId2, "user2", "password2", UserRole.MATE, emptyList())
        val usersList = listOf(user1, user2)

        every { getAllUsersUseCase(adminRole) } returns usersList

        // When
        getAllUserUI.invoke()

        // Then
        verifySequence {
            consoleIO.write("\n========================")
            consoleIO.write("║      ALL USERS        ║")
            consoleIO.write("========================")
            currentUserRole()
            getAllUsersUseCase(adminRole)
            consoleIO.write("\nList of Users:\n")
            consoleIO.write("1. ID: ${user1.id}")
            consoleIO.write("  Username: ${user1.name}")
            consoleIO.write(" Role: ${user1.role}")
            consoleIO.write("-------------------------------")
            consoleIO.write("2. ID: ${user2.id}")
            consoleIO.write("  Username: ${user2.name}")
            consoleIO.write(" Role: ${user2.role}")
            consoleIO.write("-------------------------------")
        }
    }

    @Test
    fun `should display message when no users found`() {
        // Given
        val emptyList = emptyList<User>()
        every { getAllUsersUseCase(adminRole) } returns emptyList

        // When
        getAllUserUI.invoke()

        // Then
        verifySequence {
            consoleIO.write("\n========================")
            consoleIO.write("║      ALL USERS        ║")
            consoleIO.write("========================")
            currentUserRole()
            getAllUsersUseCase(adminRole)
            consoleIO.write("ℹ️  No users found.")
        }
    }

    @Test
    fun `should use MATE role when currentUserRole returns MATE`() {
        // Given
        val mateRole = UserRole.MATE
        val usersList = listOf<User>()
        every { currentUserRole() } returns mateRole
        every { getAllUsersUseCase(mateRole) } returns usersList

        // When
        getAllUserUI.invoke()

        // Then
        verify {
            getAllUsersUseCase(mateRole)
        }
    }

    @Test
    fun `should display single user correctly`() {
        // Given
        val user = User(userId1, "singleUser", "password", UserRole.ADMIN, emptyList())
        val usersList = listOf(user)

        every { getAllUsersUseCase(adminRole) } returns usersList

        // When
        getAllUserUI.invoke()

        // Then
        verifySequence {
            consoleIO.write("\n========================")
            consoleIO.write("║      ALL USERS        ║")
            consoleIO.write("========================")
            currentUserRole()
            getAllUsersUseCase(adminRole)
            consoleIO.write("\nList of Users:\n")
            consoleIO.write("1. ID: ${user.id}")
            consoleIO.write("  Username: ${user.name}")
            consoleIO.write(" Role: ${user.role}")
            consoleIO.write("-------------------------------")
        }
    }
}