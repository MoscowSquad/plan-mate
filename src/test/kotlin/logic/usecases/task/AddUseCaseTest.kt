package logic.usecases.task

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.Task
import logic.repositories.TasksRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertTrue

class AddUseCaseTest {
    private lateinit var addTaskUseCase: AddTaskUseCase
    private lateinit var tasksRepository: TasksRepository


    @BeforeEach
    fun setup() {
        tasksRepository = mockk(relaxed = true)
        addTaskUseCase = AddTaskUseCase(tasksRepository)
    }

    @Test
    fun `should return true when add task without any issue`() {

        // Given
        val task = Task(
            id = UUID.randomUUID(),
            title = "Videos3",
            projectId = UUID.randomUUID(),
            description = "description",
            stateId = UUID.randomUUID()
        )

        every { tasksRepository.add(task) } returns true


        // When
        val result = addTaskUseCase(task)

        // Then
        assertTrue(result)
        verify(exactly = 1) { addTaskUseCase(task) }

    }

}