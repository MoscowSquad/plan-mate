package logic.usecases.tasksUseCases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.Task
import logic.repositoies.TasksRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.TaskIsNotFoundException
import java.util.*
import kotlin.test.assertTrue

class DeleteUseCaseTest {
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
    fun `should return true when task deleted successfully `() {
        // Given
        val task = Task(
            id = UUID.randomUUID(),
            title = "Videos3",
            projectId = UUID.randomUUID(),
            description = "description",
            stateId = UUID.randomUUID()
        )

        every { tasksRepository.add(task) } returns true


        // When
        val result = deleteTaskUseCase(UUID.randomUUID())

        // Then
        assertTrue(result)
        verify(exactly = 1) { deleteTaskUseCase(UUID.randomUUID()) }

    }

    @Test
    fun `should Throw TaskIsNotFoundException when wanted Id not found `() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id = id, title = "Videos", projectId = id, description = "description", stateId = id),
            Task(id = id2, title = "Videos2", projectId = id2, description = "description", stateId = id2),
        )
        every { tasksRepository.getAll() } returns tasks
        val input = UUID.fromString("00000000-0000-0000-0000-000000000003")
        // When & Then
        assertThrows<TaskIsNotFoundException> {
            deleteTaskUseCase(input)
        }
    }

    @Test
    fun `should Throw TaskIsNotFoundException when there is no tasks found to delete`() {
        // Given
        every { tasksRepository.getAll() } returns emptyList()
        val input = UUID.fromString("00000000-0000-0000-0000-000000000001")
        // When & Then
        assertThrows<TaskIsNotFoundException> {
            deleteTaskUseCase(input)
        }
    }

    @Test
    fun `should Throw TaskIsNotFoundException when no task is removed in deleteTask`() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id = id, title = "Videos", projectId = id, description = "description", stateId = id),
            Task(id = id2, title = "Videos2", projectId = id2, description = "description", stateId = id2),
        )
        every { tasksRepository.getAll() } returns tasks

        // When & Then
        assertThrows<TaskIsNotFoundException> {
            deleteTaskUseCase(UUID.fromString("00000000-0000-0000-0000-000000000003"))
        }
    }

}