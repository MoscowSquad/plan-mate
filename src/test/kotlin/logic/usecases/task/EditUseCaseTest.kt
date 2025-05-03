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
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EditUseCaseTest {
    private lateinit var editTaskUseCase: EditTaskUseCase
    private lateinit var tasksRepository: TasksRepository
    val id = UUID.fromString("00000000-0000-0000-0000-000000000001")
    val id2 = UUID.fromString("00000000-0000-0000-0000-000000000002")

    @BeforeEach
    fun setup(){
        tasksRepository = mockk(relaxed = true)
        editTaskUseCase = EditTaskUseCase(tasksRepository)
    }
    @Test
    fun `should return true when task edited successfully`() {

        // Given
        val task = Task(
            id = UUID.randomUUID(),
            title = "Videos",
            projectId = UUID.randomUUID(),
            description = "description",
            stateId = UUID.randomUUID()
        )

        every { tasksRepository.edit(task) } returns true


        // When
        val result = editTaskUseCase(task)

        // Then
        assertTrue(result)
        verify(exactly = 1) { editTaskUseCase(task) }
    }

    @Test
    fun `should false true when task is not edited successfully`() {

        // Given
        val task = Task(
            id = UUID.randomUUID(),
            title = "Videos",
            projectId = UUID.randomUUID(),
            description = "description",
            stateId = UUID.randomUUID()
        )

        every { tasksRepository.edit(task) } returns false


        // When
        val result = editTaskUseCase(task)

        // Then
        assertFalse(result)
        verify(exactly = 1) { editTaskUseCase(task) }
    }
}