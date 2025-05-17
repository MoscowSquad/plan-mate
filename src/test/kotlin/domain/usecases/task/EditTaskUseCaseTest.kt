package domain.usecases.task

import domain.models.Task
import domain.repositories.TasksRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EditTaskUseCaseTest {
    private lateinit var editTaskUseCase: EditTaskUseCase
    private lateinit var tasksRepository: TasksRepository
    val id: UUID = UUID.fromString("00000000-0000-0000-0000-000000000001")

    @BeforeEach
    fun setup() {
        tasksRepository = mockk(relaxed = true)
        editTaskUseCase = EditTaskUseCase(tasksRepository)
    }

    @Test
    fun `should return true when task edited successfully`() = runTest {
        // Given
        val task = Task(
            id = UUID.randomUUID(),
            title = "Videos",
            projectId = UUID.randomUUID(),
            description = "description",
            stateId = UUID.randomUUID()
        )

        coEvery { tasksRepository.editTask(task) } returns true

        // When
        val result = editTaskUseCase(task)

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { tasksRepository.editTask(task) }
    }

    @Test
    fun `should return false when task is not edited successfully`() = runTest {
        // Given
        val task = Task(
            id = UUID.randomUUID(),
            title = "Videos",
            projectId = UUID.randomUUID(),
            description = "description",
            stateId = UUID.randomUUID()
        )

        coEvery { tasksRepository.editTask(task) } returns false

        // When
        val result = editTaskUseCase(task)

        // Then
        assertFalse(result)
        coVerify(exactly = 1) { tasksRepository.editTask(task) }
    }
}