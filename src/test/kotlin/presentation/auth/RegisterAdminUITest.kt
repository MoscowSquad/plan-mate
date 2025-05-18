package presentation.auth

import domain.models.User.UserRole
import domain.usecases.auth.RegisterUseCase
import fake.FakeConsoleIO
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class RegisterAdminUITest {
    private lateinit var registerUseCase: RegisterUseCase
    private lateinit var consoleIO: FakeConsoleIO
    private lateinit var registerAdminUI: RegisterAdminUI

    @BeforeEach
    fun setUp() {
        registerUseCase = mockk(relaxed = true)
        consoleIO = FakeConsoleIO(LinkedList(listOf("test admin", "test password")))
        registerAdminUI = RegisterAdminUI(registerUseCase, consoleIO)
    }

    @Test
    fun `should register admin successfully on first attempt`() = runTest {
        // Given
        coEvery { registerUseCase("test admin", "test password", UserRole.ADMIN) } returns mockk<domain.models.User>(
            relaxed = true
        )
        // When
        registerAdminUI.invoke()

        // Then
        coVerifySequence {
            registerUseCase("test admin", "test password", UserRole.ADMIN)
        }

        val expectedOutputs = listOf(
            "🛡️ Register Admin",
            "Enter admin username: ",
            "Enter password: 🔑",
            "Admin registered successfully! 🎉",
            "✅ Welcome, test admin Let's begin by adding your first project."
        )

        expectedOutputs.forEachIndexed { index, message ->
            assert(consoleIO.outputs[index] == message) {
                "Expected '${expectedOutputs[index]}' but got '${consoleIO.outputs[index]}'"
            }
        }
    }

    @Test
    fun `should retry registration after failure`() = runTest {
        // Given
        consoleIO = FakeConsoleIO(LinkedList(listOf("existing", "pass1", "new admin", "pass2")))
        registerAdminUI = RegisterAdminUI(registerUseCase, consoleIO)

        coEvery { registerUseCase("existing", "pass1", UserRole.ADMIN) } throws Exception("Username already exists")
        coEvery {
            registerUseCase(
                "new admin",
                "pass2",
                UserRole.ADMIN
            )
        } returns mockk<domain.models.User>(relaxed = true)
        // When
        registerAdminUI.invoke()

        // Then
        coVerifySequence {
            registerUseCase("existing", "pass1", UserRole.ADMIN)
            registerUseCase("new admin", "pass2", UserRole.ADMIN)
        }

        val expectedOutputs = listOf(
            "🛡️ Register Admin",
            "Enter admin username: ",
            "Enter password: 🔑",
            "Registration failed. Username already exists ❌",
            "Enter admin username: ",
            "Enter password: 🔑",
            "Admin registered successfully! 🎉",
            "✅ Welcome, new admin Let's begin by adding your first project."
        )

        expectedOutputs.forEachIndexed { index, message ->
            assert(consoleIO.outputs[index] == message)
        }
    }

    @Test
    fun `should handle multiple registration failures before success`() = runTest {
        // Given
        consoleIO = FakeConsoleIO(
            LinkedList(
                listOf(
                    "user1", "weak",
                    "user2", "short",
                    "user3", "valid123"
                )
            )
        )
        registerAdminUI = RegisterAdminUI(registerUseCase, consoleIO)

        coEvery { registerUseCase("user1", "weak", UserRole.ADMIN) } throws Exception("Password too weak")
        coEvery { registerUseCase("user2", "short", UserRole.ADMIN) } throws Exception("Password too short")
        coEvery {
            registerUseCase(
                "user3",
                "valid123",
                UserRole.ADMIN
            )
        } returns mockk<domain.models.User>(relaxed = true)
        // When
        registerAdminUI.invoke()

        // Then
        coVerify {
            registerUseCase("user1", "weak", UserRole.ADMIN)
            registerUseCase("user2", "short", UserRole.ADMIN)
            registerUseCase("user3", "valid123", UserRole.ADMIN)
        }

        assert(consoleIO.outputs.contains("Registration failed. Password too weak ❌"))
        assert(consoleIO.outputs.contains("Registration failed. Password too short ❌"))
        assert(consoleIO.outputs.contains("Admin registered successfully! 🎉"))
        assert(consoleIO.outputs.contains("✅ Welcome, user3 Let's begin by adding your first project."))
    }
}
