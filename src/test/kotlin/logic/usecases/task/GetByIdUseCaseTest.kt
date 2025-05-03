package logic.usecases.task

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.Task
import logic.repositories.TasksRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.TaskIsNotFoundException
import java.util.*
import kotlin.test.assertTrue

class GetByIdUseCaseTest {
    private lateinit var getTaskByIdUseCase: GetTaskByIdUseCase
    private lateinit var tasksRepository: TasksRepository
    val id2 = UUID.fromString("00000000-0000-0000-0000-000000000002")

    @BeforeEach
    fun setup() {
        tasksRepository = mockk(relaxed = true)
        getTaskByIdUseCase = GetTaskByIdUseCase(tasksRepository)
    }

    @Test
    fun `should return task model when found task successfully `() {

        // Given
        val id = UUID.fromString("00000000-0000-0000-0000-000000000001")

        val task = Task(
            id = id,
            title = "Videos",
            projectId = UUID.randomUUID(),
            description = "description",
            stateId = UUID.randomUUID()
        )

        every { tasksRepository.getById(id) } returns task


        // When
        val result = getTaskByIdUseCase(id)

        // Then
        assertEquals(result,task)
        verify(exactly = 1) { getTaskByIdUseCase(id) }
    }
}