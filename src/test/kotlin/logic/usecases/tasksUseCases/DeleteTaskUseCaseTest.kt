package logic.usecases.tasksUseCases

import io.mockk.every
import io.mockk.mockk
import logic.models.Task
import logic.repositoies.TasksRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.TaskIsNotFoundException
import java.util.*

class DeleteTaskUseCaseTest {
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private lateinit var tasksRepository: TasksRepository
    val id = UUID.fromString("00000000-0000-0000-0000-000000000001")
    val id2 = UUID.fromString("00000000-0000-0000-0000-000000000002")

    @BeforeEach
    fun setup() {
        tasksRepository = mockk(relaxed = true)
        deleteTaskUseCase = DeleteTaskUseCase(tasksRepository)
    }

    @Test
    fun `should return list of Tasks when task deleted successfully `() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id=id, title = "Videos",projectId=id,stateId = id),
            Task(id=id2, title = "Videos2",projectId=id2,stateId = id2),
        )
        val inputTask = id
        every { tasksRepository.getAll() } returns tasks

        val expected = listOf(
            Task(id = id2, title = "Videos2",projectId=id2,stateId = id2)
        )

        // When
        val result = deleteTaskUseCase.deleteTask(inputTask)

        // Then
        assertEquals(result, expected)
    }

    @Test
    fun `should Throw exception when task ID is null during deleting `() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id=id, title = "Videos",projectId=id,stateId = id),
            Task(id=id2, title = "Videos2",projectId=id2,stateId = id2),
        )
        every { tasksRepository.getAll() } returns tasks
        val input = null
        // When & Then
        assertThrows<Exception> {
            deleteTaskUseCase.deleteTask(input)
        }
    }

    @Test
    fun `should Throw TaskIsNotFoundException when wanted Id not found `() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id=id, title = "Videos",projectId=id,stateId = id),
            Task(id=id2, title = "Videos2",projectId=id2,stateId = id2),
        )
        every { tasksRepository.getAll() } returns tasks
        val input = UUID.fromString("00000000-0000-0000-0000-000000000003")
        // When & Then
        assertThrows<TaskIsNotFoundException> {
            deleteTaskUseCase.deleteTask(input)
        }
    }

    @Test
    fun `should Throw TaskIsNotFoundException when there is no tasks found to delete`() {
        // Given
        every { tasksRepository.getAll() } returns emptyList()
        val input = UUID.fromString("00000000-0000-0000-0000-000000000001")
        // When & Then
        assertThrows<TaskIsNotFoundException> {
            deleteTaskUseCase.deleteTask(input)
        }
    }

    @Test
    fun `should Throw TaskIsNotFoundException when no task is removed in deleteTask`() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id=id, title = "Videos",projectId=id,stateId = id),
            Task(id=id2, title = "Videos2",projectId=id2,stateId = id2),
        )
        every { tasksRepository.getAll() } returns tasks

        // When & Then
        assertThrows<TaskIsNotFoundException> {
            deleteTaskUseCase.deleteTask(UUID.fromString("00000000-0000-0000-0000-000000000003"))
        }
    }
}