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
import kotlin.test.assertEquals

class GetSubTasksByTaskIdUseCaseTest {
    private lateinit var getSubTasksByTaskIdUseCase: GetSubTasksByTaskIdUseCase
    private lateinit var subTaskRepository: SubTaskRepository

    @BeforeEach
    fun setup() {
        subTaskRepository = mockk()
        getSubTasksByTaskIdUseCase = GetSubTasksByTaskIdUseCase(subTaskRepository)
    }

    @Test
    fun `should return list of subtasks when repository returns data`() = runTest {
        // Given
        val taskId = UUID.randomUUID()
        val subTasks = listOf(
            SubTask(
                id = UUID.randomUUID(),
                title = "SubTask 1",
                description = "Description 1",
                isCompleted = false,
                parentTaskId = taskId
            ),
            SubTask(
                id = UUID.randomUUID(),
                title = "SubTask 2",
                description = "Description 2",
                isCompleted = true,
                parentTaskId = taskId
            )
        )
        coEvery { subTaskRepository.getSubTasksByTaskId(taskId) } returns subTasks

        // When
        val result = getSubTasksByTaskIdUseCase(taskId)

        // Then
        assertEquals(subTasks, result)
        coVerify(exactly = 1) { subTaskRepository.getSubTasksByTaskId(taskId) }
    }

    @Test
    fun `should return empty list when repository returns no data`() = runTest {
        // Given
        val taskId = UUID.randomUUID()
        val emptyList = emptyList<SubTask>()
        coEvery { subTaskRepository.getSubTasksByTaskId(taskId) } returns emptyList

        // When
        val result = getSubTasksByTaskIdUseCase(taskId)

        // Then
        assertEquals(emptyList, result)
        coVerify(exactly = 1) { subTaskRepository.getSubTasksByTaskId(taskId) }
    }

    @Test
    fun `should pass correct taskId to repository`() = runTest {
        // Given
        val taskId = UUID.randomUUID()
        coEvery { subTaskRepository.getSubTasksByTaskId(any()) } returns emptyList()

        // When
        getSubTasksByTaskIdUseCase(taskId)

        // Then
        coVerify {
            subTaskRepository.getSubTasksByTaskId(match {
                it == taskId
            })
        }
    }
}