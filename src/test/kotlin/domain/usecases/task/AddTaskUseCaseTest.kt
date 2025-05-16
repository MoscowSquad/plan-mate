package domain.usecases.task

import domain.models.Task
import domain.repositories.TasksRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertTrue

class AddTaskUseCaseTest {
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

        every { tasksRepository.addTask(task) } returns true


        // When
        val result = addTaskUseCase(task)

        // Then
        assertTrue(result)
        verify(exactly = 1) { addTaskUseCase(task) }

    }

}