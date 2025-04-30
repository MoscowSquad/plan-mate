package logic.usecases


import io.mockk.every
import io.mockk.mockk
import logic.models.State
import logic.models.Task
import logic.repositoies.TasksRepository
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class TasksUseCaseTest{
    private lateinit var tasksUseCase: TasksUseCase
    private lateinit var tasksRepository: TasksRepository

    @BeforeEach
    fun setup(){
        tasksRepository = mockk(relaxed = true)
        tasksUseCase = TasksUseCase(tasksRepository)
    }

// Add task test cases
    @Test
fun `should return list of Tasks when add task without any issue`() {
    // Given
    every { tasksRepository.getAllTasks() }returns listOf(Task(taskID =1234 , taskTitle ="Videos1"))
    val task=Task(taskID =12345 , taskTitle ="Videos2")
    val expected = listOf(Task(taskID =1234 , taskTitle ="Videos1"),task)
    // When
    val result = tasksUseCase.addTask(task)
    // Then
    assertEquals(result,expected)
}
    @Test
    fun `should Throw exception when user title is null `() {
        // Given
        val title = null
        val stateId = 1234
        val state = "TODO"

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.addTask(Task(title = title, stateId = stateId, state = state))
        }
    }
    @Test
    fun `should Throw exception when state ID is null `() {
        // Given
        val title = "Videos"
        val stateId = null
        val state = "TODO"

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.addTask(Task(title = title, stateId = stateId, state = state))
        }
    }
    @Test
    fun `should Throw exception when user state is null `() {
        // Given
        val title = "Videos"
        val stateId = 1234
        val state = null

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.addTask(Task(title = title, stateId = stateId, state = state))
        }
    }

    // Edit task test cases
    @Test
    fun `should return task ID and Task state when edit task without any issue `() {
        // Given
        val taskId = 123
        every { tasksRepository.gelAllTasks() } returns listOf(
            Task(id = taskId),
            Task(id = 124)
        )
        // When
        val result = tasksUseCase.editTasks(taskId)
        // Then
        assertTrue(result)
    }
    @Test
    fun `should Throw exception when user title is null during editing `() {
        // Given
        every { tasksRepository.gelAllTasks() } returns listOf(
            Task(id = 1234, title = null),
            Task(id = 124, title = "Videos")
        )
        val inputId = 1234

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.editTasks(inputId)
        }
    }
    @Test
    fun `should Throw exception when task ID is null during editing `() {
        // Given
        every { tasksRepository.gelAllTasks() } returns listOf(
            Task(id = 1234, title = "Task"),
            Task(id = 124, title = "Videos")
        )
        val inputId = null

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.editTasks(inputId)
        }
    }
    @Test
    fun `should Throw exception when there is no tasks found to edit `() {
        // Given
        every { tasksRepository.gelAllTasks() } returns emptyList()
        val inputId = 123

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.editTasks(inputId)
        }
    }

    // Delete task test cases
    @Test
    fun `should return task ID when task deleted successfully `() {
        // Given
        val taskId = 123
        every { tasksRepository.gelAllTasks() } returns listOf(
            Task(id = taskId),
            Task(id = 124)
        )
        // When
        val result = tasksUseCase.deleteTask(taskId)
        // Then
        assertTrue(result)
    }
    @Test
    fun `should Throw exception when task ID is null during deleting `() {
        // Given
        every { tasksRepository.gelAllTasks() } returns listOf(
            Task(id = 1234, title = "Task"),
            Task(id = 124, title = "Videos")
        )
        val inputId = null

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.deleteTask(inputId)
        }
    }
    @Test
    fun `should Throw exception when there is no tasks found to delete`() {
        // Given
        every { tasksRepository.gelAllTasks() } returns emptyList()
        val inputId = 1234

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.deleteTask(inputId)
        }
    }

    // change task state test cases
    @Test
    fun `should return task ID when state changed successfully `() {
        // Given
        val taskId = 123
        every { tasksRepository.gelAllTasks() } returns listOf(
            Task(id = taskId),
            Task(id = 124)
        )
        // When
        val result = tasksUseCase.changeTaskState(taskId)
        // Then
        assertTrue(result)
    }
    @Test
    fun `should Throw exception when task ID is null during change state `() {
        // Given
        every { tasksRepository.gelAllTasks() } returns listOf(
            Task(id = 1234, title = "Task"),
            Task(id = 124, title = "Videos")
        )
        val inputId = null

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.changeTaskState(inputId)
        }
    }
    @Test
    fun `should Throw exception when state ID is null during change state `() {
        // Given
        every { tasksRepository.gelAllTasks() } returns listOf(
            Task(id = 1234, title = "Task", stateId = null),
            Task(id = 124, title = "Videos", stateId = 890)
        )
        val inputId = 1234

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.changeTaskState(inputId)
        }
    }
    @Test
    fun `should Throw exception when title is null during change state `() {
        // Given
        every { tasksRepository.gelAllTasks() } returns listOf(
            Task(id = 1234, title = null),
            Task(id = 124, title = "Videos")
        )
        val inputId = 1234

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.changeTaskState(inputId)
        }
    }
    @Test
    fun `should Throw exception when there is no tasks found to change state`() {
        // Given
        every { tasksRepository.gelAllTasks() } returns emptyList()
        val inputId = 1234

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.changeTaskState(inputId)
        }
    }

    // Delete all tasks test cases
    @Test
    fun `should return true when all tasks deleted successfully `() {
        // Given
        every { tasksRepository.gelAllTasks() } returns listOf(
            Task(id = 1234),
            Task(id = 1245)
        )
        // When
        val result = tasksUseCase.deleteAllTasks(true)
        // Then
        assertTrue(result)
    }
    @Test
    fun `should Throw exception when there is no tasks found `() {
        // Given
        every { tasksRepository.gelAllTasks() } returns emptyList()

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.deleteAllTasks(true)
        }
    }
    @Test
    fun `should return exception when user is not admin`() {
        // Given
        every { tasksRepository.gelAllTasks() } returns listOf(
            Task(id = 1234, title = "Task"),
            Task(id = 124, title = "Videos")
        )

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.deleteAllTasks(isAdmin = false)
        }
    }

    // Get task by ID
    @Test
    fun `should return task model when found task successfully `() {
        // Given
        every { tasksRepository.gelAllTasks() } returns listOf(
            Task(id = 1234, title = "Task", state = "TODO"),
            Task(id = 1245, title = "Videos", state = "Finished")
        )
        val inputId = 1234
        // When
        val result = tasksUseCase.getTaskById(inputId)
        // Then
        assertTrue(result)
    }
    @Test
    fun `should Throw exception when there is no tasks found during search`() {
        // Given
        every { tasksRepository.gelAllTasks() } returns listOf(
            Task(id = 1234, title = "Task"),
            Task(id = 124, title = "Videos")
        )
        val inputId = 5678

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.getTaskById(inputId)
        }
    }
    @Test
    fun `should Throw exception when state ID is null during change search `() {
        // Given
        every { tasksRepository.gelAllTasks() } returns listOf(
            Task(id = 1234, title = "Task", stateId = null),
            Task(id = 124, title = "Videos")
        )
        val inputId = 1234

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.getTaskById(inputId)
        }
    }
}
