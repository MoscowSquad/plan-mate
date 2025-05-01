package logic.usecases.tasksUseCases

import io.mockk.every
import io.mockk.mockk
import logic.models.Task
import logic.repositoies.TasksRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.PropertyNullException
import utilities.TaskIsNotFoundException

class EditTaskUseCaseTest{
    private lateinit var editTaskUseCase: EditTaskUseCase
    private lateinit var tasksRepository: TasksRepository

    @BeforeEach
    fun setup(){
        tasksRepository = mockk(relaxed = true)
        editTaskUseCase = EditTaskUseCase(tasksRepository)
    }
    @Test
    fun `should return list of Tasks when edit title without any issue`() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id = 1234, title = "Videos1"),
            Task(id = 12345, title = "Videos2")
        )
        val inputTask = Task(id = 1234, title = "Book")
        every { tasksRepository.getAll() } returns tasks

        val expected = Task(id = 1234, title = "Book")

        // When
        val result = editTaskUseCase.editTask(inputTask)

        // Then
        assertEquals(result, expected)
    }
    @Test
    fun `should Throw PropertyNullException when user title is null during editing`() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id = 1234, title = "Videos1"),
            Task(id = 12345, title = "Videos2")
        )
        every { tasksRepository.getAll() } returns tasks

        // When & Then
        assertThrows<PropertyNullException> {
            editTaskUseCase.editTask(Task(id = 12345, title = null))
        }
    }
    @Test
    fun `should Throw PropertyNullException when task ID is null during editing `() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id = 1234, title = "Videos1"),
            Task(id = 12345, title = "Videos2")
        )
        every { tasksRepository.getAll() } returns tasks

        // When & Then
        assertThrows<PropertyNullException> {
            editTaskUseCase.editTask(Task(id = null, title = "Videos3"))
        }
    }
    @Test
    fun `should Throw TaskIsNotFoundException when there is no tasks found to edit`() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id = null, title = "Videos1"),
            Task(id = 12345, title = "Videos2")
        )
        every { tasksRepository.getAll() } returns tasks

        // When & Then
        assertThrows<TaskIsNotFoundException> {
            editTaskUseCase.editTask(Task(id = 456, title = "Videos3"))
        }
    }
}