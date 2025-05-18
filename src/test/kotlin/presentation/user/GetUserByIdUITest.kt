package presentation.user

import domain.models.User
import domain.models.User.UserRole
import domain.usecases.task.GetTaskByIdUseCase
import domain.usecases.user.GetUserByIdUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class GetUserByIdUITest {
    private lateinit var getUserByIdUseCase: GetUserByIdUseCase
    private lateinit var getTaskStatesByIdUseCase: GetTaskByIdUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var getUserByIdUI: GetUserByIdUI
    private val validUuidString = "123e4567-e89b-12d3-a456-426614174000"
    private val validUserId = UUID.fromString(validUuidString)

    @BeforeEach
    fun setUp() {
        getUserByIdUseCase = mockk()
        getTaskStatesByIdUseCase = mockk()
        consoleIO = mockk(relaxed = true)
        getUserByIdUI = GetUserByIdUI(getUserByIdUseCase, getTaskStatesByIdUseCase, consoleIO)
    }

    @Test
    fun `when valid UUID is entered, should display detailed user information`() = runTest {
        // Given
        val user = mockk<User>()
        val projectIds = setOf(UUID.randomUUID(), UUID.randomUUID()).toList()

        coEvery { user.id } returns validUserId
        coEvery { user.name } returns "John Doe"
        coEvery { user.role } returns UserRole.ADMIN
        coEvery { user.projectIds } returns projectIds
        coEvery { consoleIO.write(any()) } just runs
        coEvery { consoleIO.read() } returns validUuidString
        coEvery { getUserByIdUseCase(validUserId) } returns user

        // When
        getUserByIdUI()

        // Then
        coVerifySequence {
            consoleIO.write("\nEnter user ID:")
            consoleIO.read()
            getUserByIdUseCase(validUserId)
            consoleIO.write("\n=== User Details ===")
            consoleIO.write("ID: $validUserId")
            consoleIO.write("Name: John Doe")
            consoleIO.write("Role: ADMIN")
            consoleIO.write("Projects: ${projectIds.joinToString()}")
        }
    }

    @Test
    fun `when invalid UUID is entered, should display error message`() = runTest {
        // Given
        val invalidUuid = "not-a-uuid"
        coEvery { consoleIO.read() } returns invalidUuid

        // When
        getUserByIdUI()

        // Then
        coVerifySequence {
            consoleIO.write("\nEnter user ID:")
            consoleIO.read()
            consoleIO.write("Error: Invalid UUID format")
        }
    }

    @Test
    fun `when user not found, should display user not found error message`() = runTest {
        // Given
        coEvery { consoleIO.read() } returns validUuidString
        coEvery { getUserByIdUseCase(validUserId) } throws NoSuchElementException("User not found")

        // When
        getUserByIdUI()

        // Then
        coVerifySequence {
            consoleIO.write("\nEnter user ID:")
            consoleIO.read()
            getUserByIdUseCase(validUserId)
            consoleIO.write("Error: User with ID $validUserId not found")
        }
    }

    @Test
    fun `when unexpected exception occurs, should display generic error message`() = runTest {
        // Given
        coEvery { consoleIO.read() } returns validUuidString
        coEvery { getUserByIdUseCase(validUserId) } throws RuntimeException("Something went wrong")

        // When
        getUserByIdUI()

        // Then
        coVerifySequence {
            consoleIO.write("\nEnter user ID:")
            consoleIO.read()
            getUserByIdUseCase(validUserId)
            consoleIO.write("Error: An unexpected error occurred while fetching user details")
        }
    }
}
