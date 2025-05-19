package presentation.user

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO

class UserUITest {
    private lateinit var createUserUI: CreateUserUI
    private lateinit var getAllUserUI: GetAllUserUI
    private lateinit var assignProjectToUserUI: AssignProjectToUserUI
    private lateinit var getUserByIdUI: GetUserByIdUI
    private lateinit var deleteUserUI: DeleteUserUI
    private lateinit var consoleIO: ConsoleIO
    private lateinit var userUI: UserUI

    @BeforeEach
    fun setUp() {
        createUserUI = mockk(relaxed = true)
        getAllUserUI = mockk(relaxed = true)
        assignProjectToUserUI = mockk(relaxed = true)
        getUserByIdUI = mockk(relaxed = true)
        deleteUserUI = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        userUI = UserUI(
            createUserUI,
            getAllUserUI,
            assignProjectToUserUI,
            getUserByIdUI,
            deleteUserUI,
            consoleIO
        )
    }

    @Test
    fun `should display menu and return when option 5 is selected`() = runTest {
        // Given
        every { consoleIO.read() } returns "5"

        // When
        userUI.invoke()

        // Then
        coVerifySequence {
            getAllUserUI.invoke()
            consoleIO.write("\n========== User Management ==========")
            consoleIO.write("1. ➕ Add User")
            consoleIO.write("2. 🗑️  Delete User")
            consoleIO.write("3. 📎 Assign Project to User")
            consoleIO.write("4. 🔍 Get User Details by ID")
            consoleIO.write("5. 🔙 Back")
            consoleIO.write("Enter your option (1–5): ")
            consoleIO.read()
            consoleIO.write("🔙 Returning to the previous menu...")
        }
    }

    @Test
    fun `should invoke createUserUI when option 1 is selected`() = runTest {
        // Given
        coEvery { consoleIO.read() } returnsMany listOf("1", "5")

        // When
        userUI.invoke()

        // Then
        coVerify(exactly = 1) { createUserUI.invoke() }
        coVerify(exactly = 2) { getAllUserUI.invoke() }
    }

    @Test
    fun `should invoke deleteUserUI when option 2 is selected`() = runTest {
        // Given
        coEvery { consoleIO.read() } returnsMany listOf("2", "5")

        // When
        userUI.invoke()

        // Then
        coVerify(exactly = 1) { deleteUserUI.invoke() }
        coVerify(exactly = 2) { getAllUserUI.invoke() }
    }

    @Test
    fun `should invoke assignProjectToUserUI when option 3 is selected`() = runTest {
        // Given
        coEvery { consoleIO.read() } returnsMany listOf("3", "5")

        // When
        userUI.invoke()

        // Then
        coVerify(exactly = 1) { assignProjectToUserUI.invoke() }
        coVerify(exactly = 2) { getAllUserUI.invoke() }
    }

    @Test
    fun `should invoke getUserByIdUI when option 4 is selected`() = runTest {
        // Given
        coEvery { consoleIO.read() } returnsMany listOf("4", "5")

        // When
        userUI.invoke()

        // Then
        coVerify(exactly = 1) { getUserByIdUI.invoke() }
        coVerify(exactly = 2) { getAllUserUI.invoke() }
    }

    @Test
    fun `should handle invalid input`() = runTest {
        // Given
        coEvery { consoleIO.read() } returnsMany listOf("invalid", "5")

        // When
        userUI.invoke()

        // Then
        coVerifySequence {
            getAllUserUI.invoke()
            consoleIO.write("\n========== User Management ==========")
            consoleIO.write("1. ➕ Add User")
            consoleIO.write("2. 🗑️  Delete User")
            consoleIO.write("3. 📎 Assign Project to User")
            consoleIO.write("4. 🔍 Get User Details by ID")
            consoleIO.write("5. 🔙 Back")
            consoleIO.write("Enter your option (1–5): ")
            consoleIO.read()
            consoleIO.write("❌ Invalid input. Please enter a number between 1 and 5.")

            // Second loop (exit)
            getAllUserUI.invoke()
            consoleIO.write("\n========== User Management ==========")
            consoleIO.write(any())
            consoleIO.write(any())
            consoleIO.write(any())
            consoleIO.write(any())
            consoleIO.write(any())
            consoleIO.write("Enter your option (1–5): ")
            consoleIO.read()
            consoleIO.write("🔙 Returning to the previous menu...")
        }
    }

    @Test
    fun `should handle out-of-range input`() = runTest {
        // Given
        coEvery { consoleIO.read() } returnsMany listOf("6", "5")

        // When
        userUI.invoke()

        // Then
        coVerify(exactly = 1) { consoleIO.write("❌ Invalid input. Please enter a number between 1 and 5.") }
    }

    @Test
    fun `should loop through menu until exit option is selected`() = runTest {
        // Given
        coEvery { consoleIO.read() } returnsMany listOf("1", "2", "3", "4", "5")

        // When
        userUI.invoke()

        // Then
        coVerify(exactly = 1) { createUserUI.invoke() }
        coVerify(exactly = 1) { deleteUserUI.invoke() }
        coVerify(exactly = 1) { assignProjectToUserUI.invoke() }
        coVerify(exactly = 1) { getUserByIdUI.invoke() }
        coVerify(exactly = 5) { getAllUserUI.invoke() }
    }
}