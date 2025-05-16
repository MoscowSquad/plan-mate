package domain.usecases.task

import domain.models.Task
import domain.repositories.TasksRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DeleteTaskUseCaseTest {
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private lateinit var tasksRepository: TasksRepository

    @BeforeEach
    fun setup() {
        tasksRepository = mockk(relaxed = true)
        deleteTaskUseCase = DeleteTaskUseCase(tasksRepository)
    }

    @Test
    fun `should return true when task deleted successfully `() {
        // Given
        val id = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val task = Task(
            id = id,
            title = "Videos3",
            projectId = UUID.randomUUID(),
            description = "description",
            stateId = UUID.randomUUID()
        )

        every { tasksRepository.deleteTask(id) } returns true


        // When
        val result = deleteTaskUseCase(id)

        // Then
        assertTrue(result)
        verify(exactly = 1) { deleteTaskUseCase(id) }

    }

    @Test
    fun `should return false when task is not deleted successfully `() {
        // Given
        val id = UUID.fromString("00000000-0000-0000-0000-000000000001")

        val task = Task(
            id = id,
            title = "Videos3",
            projectId = UUID.randomUUID(),
            description = "description",
            stateId = UUID.randomUUID()
        )

        every { tasksRepository.deleteTask(id) } returns false


        // When
        val result = deleteTaskUseCase(id)

        // Then
        assertFalse(result)
        verify(exactly = 1) { deleteTaskUseCase(id) }

    }

}