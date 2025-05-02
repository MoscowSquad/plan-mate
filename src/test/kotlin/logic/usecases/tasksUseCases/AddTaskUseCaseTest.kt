package logic.usecases.tasksUseCases

import io.mockk.every
import io.mockk.mockk
import logic.models.Task
import logic.repositoies.TasksRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class AddTaskUseCaseTest {
    private lateinit var addTaskUseCase: AddTaskUseCase
    private lateinit var tasksRepository: TasksRepository
    val id = UUID.fromString("00000000-0000-0000-0000-000000000001")
    val id2: UUID = UUID.fromString("00000000-0000-0000-0000-000000000002")

    @BeforeEach
    fun setup() {
        tasksRepository = mockk(relaxed = true)
        addTaskUseCase = AddTaskUseCase(tasksRepository)
    }
//UUID.fromString("00000000-0000-0000-0000-000000000002")
    @Test
    fun `should return list of Tasks when add task without any issue`() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id = id, title = "Videos", description = "description", projectId = id, stateId = id),
            Task(id = id2, title = "Videos2", description = "description", projectId = id2, stateId = id2),
            )
        val task = Task(id = UUID.fromString("00000000-0000-0000-0000-000000000003"),
            title = "Videos3",
            projectId = id2,
            description = "description",
            stateId = id2
        )

        every { tasksRepository.getAll() } returns tasks

        val expected = listOf(
            Task(id = id, title = "Videos", projectId = id, description = "description", stateId = id),
            Task(id = id2, title = "Videos2", projectId = id2, description = "description", stateId = id2),
            task,
        )

        // When
        val result = addTaskUseCase.addTask(task)
        // Then
        assertEquals(result, expected)
    }
}