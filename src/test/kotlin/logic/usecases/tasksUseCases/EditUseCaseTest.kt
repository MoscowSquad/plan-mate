package logic.usecases.tasksUseCases

import io.mockk.every
import io.mockk.mockk
import logic.models.Task
import logic.repositoies.TasksRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.TaskIsNotFoundException
import java.util.*

class EditUseCaseTest {
    private lateinit var editTaskUseCase: EditTaskUseCase
    private lateinit var tasksRepository: TasksRepository
    val id = UUID.fromString("00000000-0000-0000-0000-000000000001")
    val id2 = UUID.fromString("00000000-0000-0000-0000-000000000002")

    @BeforeEach
    fun setup(){
        tasksRepository = mockk(relaxed = true)
        editTaskUseCase = EditTaskUseCase(tasksRepository)
    }
    @Test
    fun `should return list of Tasks when edit title without any issue`() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id = id, title = "Videos", projectId = id, description = "description", stateId = id),
            Task(id = id2, title = "Videos2", projectId = id2, description = "description", stateId = id2),
        )
        val inputTask = Task(id = id2, title = "Book", projectId = id2, description = "description", stateId = id2)
        every { tasksRepository.getAll() } returns tasks

        // When
        val result = editTaskUseCase(inputTask)

        // Then
        assertEquals(result, inputTask)
    }

    @Test
    fun `should Throw TaskIsNotFoundException when there is no tasks found to edit`() {
        // Given
        val tasks: List<Task> = listOf(

            Task(id = id, title = "Videos", projectId = id, description = "description", stateId = id),
            Task(id = id2, title = "Videos2", projectId = id2, description = "description", stateId = id2),
        )
        every { tasksRepository.getAll() } returns tasks

        // When & Then
        assertThrows<TaskIsNotFoundException> {
            editTaskUseCase(
                Task(
                    id = UUID.fromString("00000000-0000-0000-0000-000000000003"),
                    title = "Videos2",
                    projectId = id2,
                    description = "description",
                    stateId = id2
                ),
            )
        }
    }
}