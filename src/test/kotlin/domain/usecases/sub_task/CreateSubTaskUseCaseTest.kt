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

class CreateSubTaskUseCaseTest {
    private lateinit var createSubTaskUseCase: CreateSubTaskUseCase
    private lateinit var subTaskRepository: SubTaskRepository

    @BeforeEach
    fun setup() {
        subTaskRepository = mockk()
        createSubTaskUseCase = CreateSubTaskUseCase(subTaskRepository)
    }

    @Test
    fun `should return true when create subtask without any issue`() = runTest {
        // Given
        val subTask = SubTask(
            id = UUID.randomUUID(),
            title = "SubTask Title",
            description = "SubTask Description",
            isCompleted = false,
            parentTaskId = UUID.randomUUID()
        )
        coEvery { subTaskRepository.createSubTask(subTask) } returns true

        // When
        val result = createSubTaskUseCase(subTask)

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { subTaskRepository.createSubTask(subTask) }
    }

    @Test
    fun `should return false when repository fails to create subtask`() = runTest {
        // Given
        val subTask = SubTask(
            id = UUID.randomUUID(),
            title = "SubTask Title",
            description = "SubTask Description",
            isCompleted = false,
            parentTaskId = UUID.randomUUID()
        )
        coEvery { subTaskRepository.createSubTask(subTask) } returns false

        // When
        val result = createSubTaskUseCase(subTask)

        // Then
        assertFalse(result)
        coVerify(exactly = 1) { subTaskRepository.createSubTask(subTask) }
    }

    @Test
    fun `should pass correct SubTask object to repository`() = runTest {
        // Given
        val subTaskId = UUID.randomUUID()
        val taskId = UUID.randomUUID()
        val subTask = SubTask(
            id = subTaskId,
            title = "Test SubTask",
            description = "Test Description",
            isCompleted = true,
            parentTaskId = taskId
        )
        coEvery { subTaskRepository.createSubTask(any()) } returns true

        // When
        createSubTaskUseCase(subTask)

        // Then
        coVerify {
            subTaskRepository.createSubTask(match {
                it.id == subTaskId && it.title == "Test SubTask" &&
                        it.parentTaskId == taskId &&
                        it.isCompleted == true
            })
        }
    }
}