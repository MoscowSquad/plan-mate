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

class GetTaskByIdUseCaseTest {
    private lateinit var getTaskByIdUseCase: GetTaskByIdUseCase
    private lateinit var tasksRepository: TasksRepository

    @BeforeEach
    fun setup() {
        tasksRepository = mockk(relaxed = true)
        getTaskByIdUseCase = GetTaskByIdUseCase(tasksRepository)
    }

    @Test
    fun `should return task model when found task successfully`() = runTest {
        // Given
        val id = UUID.fromString("00000000-0000-0000-0000-000000000001")

        val task = Task(
            id = id,
            title = "Videos",
            projectId = UUID.randomUUID(),
            description = "description",
            stateId = UUID.randomUUID()
        )

        coEvery { tasksRepository.getTaskById(id) } returns task

        // When
        val result = getTaskByIdUseCase(id)

        // Then
        assertEquals(task, result)
        coVerify(exactly = 1) { tasksRepository.getTaskById(id) }
    }
}