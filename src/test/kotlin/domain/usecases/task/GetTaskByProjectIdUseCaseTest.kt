package domain.usecases.task

import domain.models.Task
import domain.repositories.TasksRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class GetTaskByProjectIdUseCaseTest {
    private lateinit var getTaskByProjectIdUseCase: GetTaskByProjectIdUseCase
    private lateinit var tasksRepository: TasksRepository

    @BeforeEach
    fun setup() {
        tasksRepository = mockk(relaxed = true)
        getTaskByProjectIdUseCase = GetTaskByProjectIdUseCase(tasksRepository)
    }

    @Test
    fun `should return list of Task model when found project successfully`() = runTest {
        // Given
        val projectId = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val tasks: List<Task> = listOf(
            Task(
                id = UUID.randomUUID(),
                title = "Videos",
                projectId = projectId,
                description = "description",
                stateId = UUID.randomUUID()
            ),
            Task(
                id = UUID.randomUUID(),
                title = "Videos2",
                projectId = projectId,
                description = "description",
                stateId = UUID.randomUUID()
            )
        )
        coEvery { tasksRepository.getTaskByProjectId(projectId) } returns tasks

        // When
        val result = getTaskByProjectIdUseCase(projectId)

        // Then
        assertEquals(tasks, result)
        coVerify(exactly = 1) { tasksRepository.getTaskByProjectId(projectId) }
    }
}