package logic.usecases.task

import io.mockk.every
import io.mockk.mockk
import logic.models.Task
import logic.repositories.TasksRepository
import logic.util.TaskIsNotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import org.junit.jupiter.api.assertThrows

class GetTaskByProjectIdUseCaseTest {
    private lateinit var getTaskByProjectIdUseCase: GetTaskByProjectIdUseCase
    private lateinit var tasksRepository: TasksRepository
    val id = UUID.fromString("00000000-0000-0000-0000-000000000001")
    val id2 = UUID.fromString("00000000-0000-0000-0000-000000000002")

    @BeforeEach
    fun setup() {
        tasksRepository = mockk(relaxed = true)
        getTaskByProjectIdUseCase = GetTaskByProjectIdUseCase(tasksRepository)
    }

    @Test
    fun `should return task model when found task successfully `() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id = id, name = "Videos", projectId = id, description = "description", stateId = id),
            Task(id = id2, name = "Videos2", projectId = id2, description = "description", stateId = id2),
        )
        val input = id
        every { tasksRepository.getAllTasks() } returns tasks

        val expected = Task(id = id, name = "Videos", projectId = id, description = "description", stateId = id)

        // When
        val result = getTaskByProjectIdUseCase(input)

        // Then
        assertEquals(result, expected)
    }

    @Test
    fun `should throw TaskIsNotFoundException when wanted task not found`() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id = id, name = "Videos", projectId = id, description = "description", stateId = id),
            Task(id = id2, name = "Videos2", projectId = id2, description = "description", stateId = id2),
        )
        every { tasksRepository.getAllTasks() } returns tasks
        val input = UUID.fromString("00000000-0000-0000-0000-000000000003")

        // When & Then
        assertThrows<TaskIsNotFoundException> {
            getTaskByProjectIdUseCase(input)
        }
    }

    @Test
    fun `should Throw TaskIsNotFoundException when there is no tasks`() {
        // Given
        every { tasksRepository.getAllTasks() } returns emptyList()
        val input = UUID.fromString("00000000-0000-0000-0000-000000000002")

        // When & Then
        assertThrows<TaskIsNotFoundException> {
            getTaskByProjectIdUseCase(input)
        }
    }
}