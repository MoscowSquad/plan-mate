package presentation.auth

import fake.FakeConsoleIO
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import logic.models.UserRole
import logic.usecases.auth.RegisterUseCase
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
    fun `should register admin successfully on first attempt`() {
        // Given
        every { registerUseCase("test admin", "test password", UserRole.ADMIN) } returns mockk<logic.models.User>(relaxed = true)
        // When
        registerAdminUI.invoke()

        // Then
        verifySequence {
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
    fun `should retry registration after failure`() {
        // Given
        consoleIO = FakeConsoleIO(LinkedList(listOf("existing", "pass1", "new admin", "pass2")))
        registerAdminUI = RegisterAdminUI(registerUseCase, consoleIO)

        every { registerUseCase("existing", "pass1", UserRole.ADMIN) } throws Exception("Username already exists")
        every { registerUseCase("new admin", "pass2", UserRole.ADMIN) } returns mockk<logic.models.User>(relaxed = true)
        // When
        registerAdminUI.invoke()

        // Then
        verifySequence {
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
    fun `should handle multiple registration failures before success`() {
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

        every { registerUseCase("user1", "weak", UserRole.ADMIN) } throws Exception("Password too weak")
        every { registerUseCase("user2", "short", UserRole.ADMIN) } throws Exception("Password too short")
        every { registerUseCase("user3", "valid123", UserRole.ADMIN) } returns mockk<logic.models.User>(relaxed = true)
        // When
        registerAdminUI.invoke()

        // Then
        verify {
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