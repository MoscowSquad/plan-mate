package presentation.state

import domain.models.TaskState
import domain.usecases.task_state.GetTaskStatesByProjectIdUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class GetAllTaskStatesUITest {
    private lateinit var getTaskStatesByProjectIdUseCase: GetTaskStatesByProjectIdUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var getAllTaskStatesUI: GetAllTaskStatesUI
    private val projectId = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        getTaskStatesByProjectIdUseCase = mockk()
        consoleIO = mockk(relaxed = true)
        getAllTaskStatesUI = GetAllTaskStatesUI(getTaskStatesByProjectIdUseCase, consoleIO)
    }

    @Test
    fun `should display all states when states exist`() = runTest {
        // Given
        val state1 = TaskState(
            id = UUID.randomUUID(),
            name = "To Do",
            projectId = projectId
        )
        val state2 = TaskState(
            id = UUID.randomUUID(),
            name = "In Progress",
            projectId = projectId
        )
        val states = listOf(state1, state2)
        coEvery { getTaskStatesByProjectIdUseCase(projectId) } returns states

        // When
        getAllTaskStatesUI.invoke(projectId)

        // Then
        coVerify {
            getTaskStatesByProjectIdUseCase(projectId)
            consoleIO.write(
                """
                -------------------------------------------------------
                | State Name: ${state1.name}
                | State ID: ${state1.id}
                -------------------------------------------------------
                """.trimIndent()
            )
            consoleIO.write(
                """
                -------------------------------------------------------
                | State Name: ${state2.name}
                | State ID: ${state2.id}
                -------------------------------------------------------
                """.trimIndent()
            )
        }
    }

    @Test
    fun `should display message when no states exist`() = runTest {
        // Given
        val emptyList = emptyList<TaskState>()
        coEvery { getTaskStatesByProjectIdUseCase(projectId) } returns emptyList

        // When
        getAllTaskStatesUI.invoke(projectId)

        // Then
        coVerifySequence {
            getTaskStatesByProjectIdUseCase(projectId)
            consoleIO.write("ℹ️ No states found for this project.")
        }
    }

    @Test
    fun `should handle exception when fetching states`() = runTest {
        // Given
        val errorMessage = "Failed to fetch states"
        coEvery { getTaskStatesByProjectIdUseCase(projectId) } throws RuntimeException(errorMessage)

        // When
        getAllTaskStatesUI.invoke(projectId)

        // Then
        coVerifySequence {
            getTaskStatesByProjectIdUseCase(projectId)
            consoleIO.write("❌ Failed to fetch states: $errorMessage")
        }
    }

    @Test
    fun `should fetch states with correct project ID`() = runTest {
        // Given
        val differentProjectId = UUID.randomUUID()
        coEvery { getTaskStatesByProjectIdUseCase(differentProjectId) } returns emptyList()

        // When
        getAllTaskStatesUI.invoke(differentProjectId)

        // Then
        coVerify {
            getTaskStatesByProjectIdUseCase(differentProjectId)
        }
    }

    @Test
    fun `should display states in the correct order`() = runTest {
        // Given
        val state1 = TaskState(
            id = UUID.randomUUID(),
            name = "To Do",
            projectId = projectId
        )
        val state2 = TaskState(
            id = UUID.randomUUID(),
            name = "In Progress",
            projectId = projectId
        )
        val state3 = TaskState(
            id = UUID.randomUUID(),
            name = "Done",
            projectId = projectId
        )
        val states = listOf(state1, state2, state3)
        coEvery { getTaskStatesByProjectIdUseCase(projectId) } returns states

        // When
        getAllTaskStatesUI.invoke(projectId)

        // Then
        verifySequence {
            consoleIO.write(
                """
                -------------------------------------------------------
                | State Name: ${state1.name}
                | State ID: ${state1.id}
                -------------------------------------------------------
                """.trimIndent()
            )
            consoleIO.write(
                """
                -------------------------------------------------------
                | State Name: ${state2.name}
                | State ID: ${state2.id}
                -------------------------------------------------------
                """.trimIndent()
            )
            consoleIO.write(
                """
                -------------------------------------------------------
                | State Name: ${state3.name}
                | State ID: ${state3.id}
                -------------------------------------------------------
                """.trimIndent()
            )
        }
    }
}