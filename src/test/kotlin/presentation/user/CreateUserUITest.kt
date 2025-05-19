package presentation.user

        import data.session_manager.SessionManager
        import domain.models.User
        import domain.models.User.UserRole
        import domain.usecases.user.CreateUserUseCase
        import domain.util.isValidPasswordFormat
        import io.mockk.*
        import kotlinx.coroutines.test.runTest
        import org.junit.jupiter.api.BeforeEach
        import org.junit.jupiter.api.Test
        import presentation.io.ConsoleIO
        import java.util.*

        class CreateUserUITest {
            private lateinit var createUserUseCase: CreateUserUseCase
            private lateinit var consoleIO: ConsoleIO
            private lateinit var createUserUI: CreateUserUI

            private val username = "test user"
            private val validPassword = "Valid1Password!"
            private val userRole = UserRole.MATE
            private val currentUserRole = UserRole.ADMIN
            private val userId = UUID.randomUUID()

            @BeforeEach
            fun setUp() {
                createUserUseCase = mockk(relaxed = true)
                consoleIO = mockk(relaxed = true)
                createUserUI = CreateUserUI(createUserUseCase, consoleIO)

                mockkStatic(::isValidPasswordFormat)
                every { isValidPasswordFormat(validPassword) } returns true

                mockkObject(SessionManager)
                every { SessionManager.getCurrentUserRole() } returns currentUserRole

            }

            @Test
            fun `should create user successfully with valid inputs`() = runTest {
                // Given
                val inputSequence = listOf(username, validPassword, "2") // 2 for MATE role
                var inputIndex = 0
                every { consoleIO.read() } answers { inputSequence[inputIndex++] }

                coEvery {
                    createUserUseCase(
                        currentUserRole,
                        match<User> {
                            it.id == userId &&
                            it.name == username &&
                            it.role == userRole
                        },
                        validPassword
                    )
                }

                // When
                createUserUI.invoke()

                // Then
                coVerifySequence {
                    consoleIO.write("\n╔══════════════════════════╗")
                    consoleIO.write("║      CREATE NEW USER     ║")
                    consoleIO.write("╚══════════════════════════╝")
                    consoleIO.write("\nEnter username:")
                    consoleIO.read()
                    consoleIO.write(any()) // Password requirements
                    consoleIO.read()
                    consoleIO.write("\nSelect role:")
                    UserRole.entries.forEach { role ->
                        consoleIO.write("${role.ordinal + 1}. $role")
                    }
                    consoleIO.write("Enter choice (1-${UserRole.entries.size}):")
                    consoleIO.read()
                    createUserUseCase(currentUserRole, any(), validPassword)
                    consoleIO.write("\n✅ User '$username' created successfully!")
                }
            }

            @Test
            fun `should prompt for username until valid input is provided`() = runTest {
                // Given
                val inputSequence = listOf("", "  ", username, validPassword, "2") // First two inputs are invalid
                var inputIndex = 0
                every { consoleIO.read() } answers { inputSequence[inputIndex++] }

                // When
                createUserUI.invoke()

                // Then
                verify(exactly = 3) { consoleIO.write("\nEnter username:") }
                verify(exactly = 2) { consoleIO.write("❌ Username cannot be empty") }
            }

            @Test
            fun `should prompt for password until valid format is provided`() = runTest {
                // Given
                val invalidPassword = "weak"
                val inputSequence = listOf(username, invalidPassword, validPassword, "2")
                var inputIndex = 0
                every { consoleIO.read() } answers { inputSequence[inputIndex++] }
                every { isValidPasswordFormat(invalidPassword) } returns false

                // When
                createUserUI.invoke()

                // Then
                verify(exactly = 2) {
                    consoleIO.write(match { it.contains("At least one lowercase letter") })
                }
                verify { consoleIO.write("❌ Password must meet the specified criteria") }
            }

            @Test
            fun `should prompt for role until valid choice is provided`() = runTest {
                // Given
                val inputSequence = listOf(username, validPassword, "invalid", "0", "10", "2")
                var inputIndex = 0
                every { consoleIO.read() } answers { inputSequence[inputIndex++] }

                // When
                createUserUI.invoke()

                // Then
                verify(exactly = 4) { consoleIO.write("Enter choice (1-${UserRole.entries.size}):") }
                verify(exactly = 1) { consoleIO.write("⚠️ Please enter a valid number") }
                verify(exactly = 2) { consoleIO.write("⚠️ Please enter a number between 1 and ${UserRole.entries.size}") }
            }

            @Test
            fun `should handle exception when creating user`() = runTest {
                // Given
                val errorMessage = "Insufficient permissions"
                val inputSequence = listOf(username, validPassword, "2")
                var inputIndex = 0
                every { consoleIO.read() } answers { inputSequence[inputIndex++] }

                coEvery {
                    createUserUseCase(currentUserRole, any(), validPassword)
                } throws RuntimeException(errorMessage)

                // When
                createUserUI.invoke()

                // Then
                verify { consoleIO.write("❌ Failed to create user: $errorMessage") }
            }
        }