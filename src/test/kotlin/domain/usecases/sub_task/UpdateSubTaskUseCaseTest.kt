package domain.usecases.sub_task

import domain.models.SubTask
import domain.repositories.SubTaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UpdateSubTaskUseCaseTest {
    private lateinit var updateSubTaskUseCase: UpdateSubTaskUseCase
    private lateinit var subTaskRepository: SubTaskRepository

    @BeforeEach
    fun setup() {
        subTaskRepository = mockk()
        updateSubTaskUseCase = UpdateSubTaskUseCase(subTaskRepository)
    }

    @Test
    fun `should return true when update subtask without any issue`() = runTest {
        // Given
        val subTask = SubTask(
            id = UUID.randomUUID(),
            title = "Updated SubTask Title",
            description = "Updated SubTask Description",
            isCompleted = true,
            parentTaskId = UUID.randomUUID()
        )
        coEvery { subTaskRepository.updateSubTask(subTask) } returns true

        // When
        val result = updateSubTaskUseCase(subTask)

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { subTaskRepository.updateSubTask(subTask) }
    }

    @Test
    fun `should return false when repository fails to update subtask`() = runTest {
        // Given
        val subTask = SubTask(
            id = UUID.randomUUID(),
            title = "Updated SubTask Title",
            description = "Updated SubTask Description",
            isCompleted = true,
            parentTaskId = UUID.randomUUID()
        )
        coEvery { subTaskRepository.updateSubTask(subTask) } returns false

        // When
        val result = updateSubTaskUseCase(subTask)

        // Then
        assertFalse(result)
        coVerify(exactly = 1) { subTaskRepository.updateSubTask(subTask) }
    }

    @Test
    fun `should pass correct SubTask object to repository`() = runTest {
        // Given
        val subTaskId = UUID.randomUUID()
        val taskId = UUID.randomUUID()
        val subTask = SubTask(
            id = subTaskId,
            title = "Test Updated SubTask",
            description = "Test Updated Description",
            isCompleted = true,
            parentTaskId = taskId
        )
        coEvery { subTaskRepository.updateSubTask(any()) } returns true

        // When
        updateSubTaskUseCase(subTask)

        // Then
        coVerify {
            subTaskRepository.updateSubTask(match {
                it.id == subTaskId &&
                it.title == "Test Updated SubTask" &&
                it.description == "Test Updated Description" &&
                it.parentTaskId == taskId &&
                it.isCompleted == true
            })
        }
    }
}