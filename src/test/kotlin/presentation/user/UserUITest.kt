package presentation.user

import io.mockk.*
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
        userUI = UserUI(createUserUI, getAllUserUI, assignProjectToUserUI, getUserByIdUI, deleteUserUI, consoleIO)
    }

    @Test
    fun `should display menu and handle option 1 - add user`() {
        // Given
        every { consoleIO.read() } returns "1"

        // When
        userUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any())
            consoleIO.read()
            createUserUI()
        }
    }

    @Test
    fun `should display menu and handle option 2 - delete user`() {
        // Given
        every { consoleIO.read() } returns "2"

        // When
        userUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any())
            consoleIO.read()
            deleteUserUI()
        }
    }

    @Test
    fun `should display menu and handle option 3 - change user role`() {
        // Given
        every { consoleIO.read() } returns "3"

        // When
        userUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any())
            consoleIO.read()
            assignProjectToUserUI()
        }
    }

    @Test
    fun `should display menu and handle option 4 - get user details by id`() {
        // Given
        every { consoleIO.read() } returns "4"

        // When
        userUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any())
            consoleIO.read()
            getUserByIdUI()
        }
    }

    @Test
    fun `should display menu and handle option 5 - get all users`() {
        // Given
        every { consoleIO.read() } returns "5"

        // When
        userUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any())
            consoleIO.read()
            getAllUserUI()
        }
    }

    @Test
    fun `should display menu and handle option 6 - back`() {
        // Given
        every { consoleIO.read() } returns "6"

        // When
        userUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any())
            consoleIO.read()
        }
        verify(exactly = 0) { createUserUI() }
        verify(exactly = 0) { deleteUserUI() }
        verify(exactly = 0) { assignProjectToUserUI() }
        verify(exactly = 0) { getUserByIdUI() }
        verify(exactly = 0) { getAllUserUI() }
    }

    @Test
    fun `should display menu and handle invalid option`() {
        // Given
        every { consoleIO.read() } returns "99"

        // When
        userUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("Wrong option enter a number between 1 and 6.")
        }
    }

    @Test
    fun `should display menu and handle non-numeric input`() {
        // Given
        every { consoleIO.read() } returns "invalid"

        // When
        userUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("Wrong option enter a number between 1 and 6.")
        }
    }

    @Test
    fun `should display menu and handle empty input`() {
        // Given
        every { consoleIO.read() } returns ""

        // When
        userUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("Wrong option enter a number between 1 and 6.")
        }
    }
}