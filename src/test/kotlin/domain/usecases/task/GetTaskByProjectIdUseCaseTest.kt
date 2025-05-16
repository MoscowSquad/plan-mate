package domain.usecases.task

import domain.models.Task
import domain.repositories.TasksRepository
import io.mockk.every
import io.mockk.mockk
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
    fun `should return list of Task model when found project successfully `() {
        // Given
        val projectID = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val tasks: List<Task> = listOf(
            Task(
                id = UUID.randomUUID(),
                title = "Videos",
                projectId = projectID,
                description = "description",
                stateId = UUID.randomUUID()
            ),
            Task(
                id = UUID.randomUUID(),
                title = "Videos2",
                projectId = projectID,
                description = "description",
                stateId = UUID.randomUUID()
            ),
        )
        every { tasksRepository.getTaskByProjectId(projectID) } returns tasks


        // When
        val result = getTaskByProjectIdUseCase(projectID)

        // Then
        assertEquals(result, tasks)
    }
}