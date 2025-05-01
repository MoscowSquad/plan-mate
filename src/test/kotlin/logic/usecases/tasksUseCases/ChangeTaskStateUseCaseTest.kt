package logic.usecases.tasksUseCases

import io.mockk.every
import io.mockk.mockk
import logic.models.State
import logic.models.Task
import logic.repositoies.TasksRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.PropertyNullException
import utilities.TaskIsNotFoundException
import java.util.*

class ChangeTaskStateUseCaseTest {
    private lateinit var changeTaskStateUseCase: ChangeTaskStateUseCase
    private lateinit var tasksRepository: TasksRepository

    @BeforeEach
    fun setup() {
        tasksRepository = mockk(relaxed = true)
        changeTaskStateUseCase = ChangeTaskStateUseCase(tasksRepository)
    }

    @Test
    fun `should return task model when state changed successfully`() {
        // Given
        val id: UUID = mockk()
        val projectId: UUID = mockk()
        val tasks: List<Task> = listOf(
            Task(id = 1234, title = "Videos1", state = State(id, "TODO", projectId)),
            Task(id = 12345, title = "Videos2", state = State(id, "TODO", projectId))
        )
        val inputTask = Task(id = 1234, state = State(id, "Done", projectId))
        every { tasksRepository.getAll() } returns tasks

        val expected = Task(id = 1234, title = "Videos1", state = State(id, "Done", projectId))

        // When
        val result = changeTaskStateUseCase.changeTaskState(inputTask)

        // Then
        assertEquals(result, expected)
    }

    @Test
    fun `should Throw PropertyNullException when task ID is null during change state `() {
        // Given
        val id: UUID = mockk()
        val projectId: UUID = mockk()
        val tasks: List<Task> = listOf(
            Task(id = 1234, title = "Videos1", state = State(id, "TODO", projectId)),
            Task(id = 12345, title = "Videos2", state = State(id, "TODO", projectId))
        )

        every { tasksRepository.getAll() } returns tasks
        val input = Task(id = null, title = "Videos1", state = State(id, "TODO", projectId))
        // When & Then
        assertThrows<PropertyNullException> {
            changeTaskStateUseCase.changeTaskState(input)
        }
    }

    @Test
    fun `should Throw TaskIsNotFoundException when wanted task is not found`() {
        // Given
        val id: UUID = mockk()
        val projectId: UUID = mockk()
        val tasks: List<Task> = listOf(
            Task(id = null, title = "Videos1", state = State(id, "TODO", projectId)),
            Task(id = 12345, title = "Videos2", state = State(id, "TODO", projectId))
        )
        every { tasksRepository.getAll() } returns tasks
        val input = Task(id = 123, title = "Videos1", state = State(id, "TODO", projectId))
        // When & Then
        assertThrows<TaskIsNotFoundException> {
            changeTaskStateUseCase.changeTaskState(input)
        }
    }

    @Test
    fun `should Throw TaskIsNotFoundException when there is no tasks found to change state`() {
        // Given
        val id: UUID = mockk()
        val projectId: UUID = mockk()
        every { tasksRepository.getAll() } returns emptyList()
        val input = Task(id = 123, title = "Videos1", state = State(id, "TODO", projectId))
        // When & Then
        assertThrows<TaskIsNotFoundException> {
            changeTaskStateUseCase.changeTaskState(input)
        }
    }

    @Test
    fun `should Throw PropertyNullException when taskToEdit has null ID`() {
        // Given
        val id: UUID = mockk()
        val projectId: UUID = mockk()
        val tasks: List<Task> = listOf(
            Task(id = 123, title = "Videos1", state = State(id, "TODO", projectId))
        )
        every { tasksRepository.getAll() } returns tasks

        // When & Then
        assertThrows<PropertyNullException> {
            changeTaskStateUseCase.changeTaskState(Task(id = null, state = State(id, "Done", projectId)))
        }
    }

}