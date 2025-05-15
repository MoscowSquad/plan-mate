package presentation.user

    import io.mockk.*
    import logic.usecases.user.GetUserByIdUseCase
    import logic.models.User
    import logic.models.User.UserRole
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
            getUserByIdUseCase = mockk()
            consoleIO = mockk(relaxed = true)
            getUserByIdUI = GetUserByIdUI(getUserByIdUseCase, consoleIO)
        }


        @Test
        fun `when valid UUID is entered, should display detailed user information`() {
            // Given
            val user = mockk<User>()
            val projectIds = setOf(UUID.randomUUID(), UUID.randomUUID()).toList()

            every { user.id } returns validUserId
            every { user.name } returns "John Doe"
            every { user.role } returns UserRole.ADMIN
            every { user.projectIds } returns projectIds
            every { consoleIO.write(any()) } just runs
            every { consoleIO.read() } returns validUuidString
            every { getUserByIdUseCase(validUserId) } returns user

            // When
            getUserByIdUI()

            // Then
            verifySequence {
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
        fun `when invalid UUID is entered, should display error message`() {
            // Given
            val invalidUuid = "not-a-uuid"
            every { consoleIO.read() } returns invalidUuid

            // When
            getUserByIdUI()

            // Then
            verifySequence {
                consoleIO.write("\nEnter user ID:")
                consoleIO.read()
                consoleIO.write("Error: Invalid UUID format")
            }
        }

        @Test
        fun `when user not found, should display user not found error message`() {
            // Given
            every { consoleIO.read() } returns validUuidString
            every { getUserByIdUseCase(validUserId) } throws NoSuchElementException("User not found")

            // When
            getUserByIdUI()

            // Then
            verifySequence {
                consoleIO.write("\nEnter user ID:")
                consoleIO.read()
                getUserByIdUseCase(validUserId)
                consoleIO.write("Error: User with ID $validUserId not found")
            }
        }

        @Test
        fun `when unexpected exception occurs, should display generic error message`() {
            // Given
            every { consoleIO.read() } returns validUuidString
            every { getUserByIdUseCase(validUserId) } throws RuntimeException("Something went wrong")

            // When
            getUserByIdUI()

            // Then
            verifySequence {
                consoleIO.write("\nEnter user ID:")
                consoleIO.read()
                getUserByIdUseCase(validUserId)
                consoleIO.write("Error: An unexpected error occurred while fetching user details")
            }
        }
    }