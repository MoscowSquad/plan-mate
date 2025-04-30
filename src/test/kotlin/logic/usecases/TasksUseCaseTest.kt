package logic.usecases

import fake.createTaskHelper
import io.mockk.every
import io.mockk.mockk
import logic.models.State
import logic.models.Task
import logic.repositoies.TasksRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*


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
    val id: UUID = mockk()
    val projectId: UUID = mockk()
    val tasks: List<Task> = listOf(
        createTaskHelper(taskID = 1234, taskTitle = "Videos1", state = State(id, "TODO", projectId)),
        createTaskHelper(taskID = 12345, taskTitle = "Videos2", state = State(id, "TODO", projectId))
    )
    val task = Task(taskID = 123456, taskTitle = "Videos3", state = State(id, "TODO", projectId))
    every { tasksRepository.getAllTasks() } returns tasks

    val expected = listOf(
        createTaskHelper(taskID = 1234, taskTitle = "Videos1", state = State(id, "TODO", projectId)),
        createTaskHelper(taskID = 12345, taskTitle = "Videos2", state = State(id, "TODO", projectId)),
        createTaskHelper(taskID = 123456, taskTitle = "Videos3", state = State(id, "TODO", projectId))
    )

    // When
    val result = tasksUseCase.addTask(task)
    // Then
    assertEquals(result, expected)
}
    @Test
    fun `should Throw exception when user title is null`() {
        // Given
        val tasks: List<Task> = listOf(
            createTaskHelper(taskID = 1234, taskTitle = "Videos1"),
            createTaskHelper(taskID = 12345, taskTitle = "Videos2")
        )
        every { tasksRepository.getAllTasks() } returns tasks

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.addTask(Task(taskID = 12345, taskTitle = null))
        }
    }
    @Test
    fun `should Throw exception when state ID is null `() {
        // Given
        val tasks: List<Task> = listOf(
            createTaskHelper(taskID = 1234, taskTitle = "Videos1"),
            createTaskHelper(taskID = 12345, taskTitle = "Videos2")
        )
        every { tasksRepository.getAllTasks() } returns tasks

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.addTask(Task(taskID = null, taskTitle = "Videos3"))
        }
    }
    @Test
    fun `should Throw exception when user state is null `() {
        // Given
        val tasks: List<Task> = listOf(
            createTaskHelper(taskID = 1234, taskTitle = "Videos1"),
            createTaskHelper(taskID = 12345, taskTitle = "Videos2")
        )
        every { tasksRepository.getAllTasks() } returns tasks

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.addTask(Task(taskID = 12345, taskTitle = "Videos3", state = null))
        }
    }

    // Edit task test cases
    @Test
    fun `should return list of Tasks when edit title without any issue`() {
        // Given
        val tasks: List<Task> = listOf(
            createTaskHelper(taskID = 1234, taskTitle = "Videos1"),
            createTaskHelper(taskID = 12345, taskTitle = "Videos2")
        )
        val inputTask = Task(taskID = 1234, taskTitle = "Book")
        every { tasksRepository.getAllTasks() } returns tasks

        val expected = Task(taskID = 1234, taskTitle = "Book")

        // When
        val result = tasksUseCase.editTask(inputTask)

        // Then
        assertEquals(result, expected)
    }
    @Test
    fun `should Throw exception when user title is null during editing`() {
        // Given
        val tasks: List<Task> = listOf(
            createTaskHelper(taskID = 1234, taskTitle = "Videos1"),
            createTaskHelper(taskID = 12345, taskTitle = "Videos2")
        )
        every { tasksRepository.getAllTasks() } returns tasks

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.editTask(Task(taskID = 12345, taskTitle = null))
        }
    }
    @Test
    fun `should Throw exception when task ID is null during editing `() {
        // Given
        val tasks: List<Task> = listOf(
            createTaskHelper(taskID = 1234, taskTitle = "Videos1"),
            createTaskHelper(taskID = 12345, taskTitle = "Videos2")
        )
        every { tasksRepository.getAllTasks() } returns tasks

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.editTask(Task(taskID = null, taskTitle = "Videos3"))
        }
    }
    @Test
    fun `should Throw exception when there is no tasks found to edit`() {
        // Given
        val tasks: List<Task> = listOf(
            createTaskHelper(taskID = 1234, taskTitle = "Videos1"),
            createTaskHelper(taskID = 12345, taskTitle = "Videos2")
        )
        every { tasksRepository.getAllTasks() } returns tasks

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.addTask(Task(taskID = 456, taskTitle = "Videos3"))
        }
    }


    // Delete task test cases
    @Test
    fun `should return list of Tasks when task deleted successfully `() {
        // Given
        val tasks: List<Task> = listOf(
            createTaskHelper(taskID = 1234, taskTitle = "Videos1"),
            createTaskHelper(taskID = 12345, taskTitle = "Videos2")
        )
        val inputTask = 1234
        every { tasksRepository.getAllTasks() } returns tasks

        val expected = listOf(
            createTaskHelper(taskID = 12345, taskTitle = "Videos2")
        )

        // When
        val result = tasksUseCase.deleteTask(inputTask)

        // Then
        assertEquals(result, expected)
    }
    @Test
    fun `should Throw exception when task ID is null during deleting `() {
        // Given
        val tasks: List<Task> = listOf(
            createTaskHelper(taskID = 1234, taskTitle = "Videos1"),
            createTaskHelper(taskID = 12345, taskTitle = "Videos2")
        )
        every { tasksRepository.getAllTasks() } returns tasks
        val input = null
        // When & Then
        assertThrows<Exception> {
            tasksUseCase.deleteTask(input)
        }
    }

    @Test
    fun `should Throw exception when wanted Id not found `() {
        // Given
        val tasks: List<Task> = listOf(
            createTaskHelper(taskID = 1234, taskTitle = "Videos1"),
            createTaskHelper(taskID = 12345, taskTitle = "Videos2")
        )
        every { tasksRepository.getAllTasks() } returns tasks
        val input = 456
        // When & Then
        assertThrows<Exception> {
            tasksUseCase.deleteTask(input)
        }
    }
    @Test
    fun `should Throw exception when there is no tasks found to delete`() {
        // Given
        every { tasksRepository.getAllTasks() } returns emptyList()
        val input = 1234
        // When & Then
        assertThrows<Exception> {
            tasksUseCase.deleteTask(input)
        }
    }

    @Test
    fun `should Throw exception when no task is removed in deleteTask`() {
        // Given
        val tasks: List<Task> = listOf(
            createTaskHelper(taskID = 1111, taskTitle = "Not matching")
        )
        every { tasksRepository.getAllTasks() } returns tasks

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.deleteTask(1234)
        }
    }

    // change task state test cases
    @Test
    fun `should return task model when state changed successfully`() {
        // Given
        val id: UUID = mockk()
        val projectId: UUID = mockk()
        val tasks: List<Task> = listOf(
            createTaskHelper(taskID = 1234, taskTitle = "Videos1", state = State(id, "TODO", projectId)),
            createTaskHelper(taskID = 12345, taskTitle = "Videos2", state = State(id, "TODO", projectId))
        )
        val inputTask = Task(taskID = 1234, state = State(id, "Done", projectId))
        every { tasksRepository.getAllTasks() } returns tasks

        val expected = Task(taskID = 1234, taskTitle = "Videos1", state = State(id, "Done", projectId))

        // When
        val result = tasksUseCase.changeTaskState(inputTask)

        // Then
        assertEquals(result, expected)
    }
    @Test
    fun `should Throw exception when task ID is null during change state `() {
        // Given
        val id: UUID = mockk()
        val projectId: UUID = mockk()
        val tasks: List<Task> = listOf(
            createTaskHelper(taskID = 1234, taskTitle = "Videos1", state = State(id, "TODO", projectId)),
            createTaskHelper(taskID = 12345, taskTitle = "Videos2", state = State(id, "TODO", projectId))
        )

        every { tasksRepository.getAllTasks() } returns tasks
        val input = Task(taskID = null, taskTitle = "Videos1", state = State(id, "TODO", projectId))
        // When & Then
        assertThrows<Exception> {
            tasksUseCase.changeTaskState(input)
        }
    }

    @Test
    fun `should Throw exception when wanted task is not found`() {
        // Given
        val id: UUID = mockk()
        val projectId: UUID = mockk()
        val tasks: List<Task> = listOf(
            createTaskHelper(taskID = 1234, taskTitle = "Videos1", state = State(id, "TODO", projectId)),
            createTaskHelper(taskID = 12345, taskTitle = "Videos2", state = State(id, "TODO", projectId))
        )
        every { tasksRepository.getAllTasks() } returns tasks
        val input = Task(taskID = 123, taskTitle = "Videos1", state = State(id, "TODO", projectId))
        // When & Then
        assertThrows<Exception> {
            tasksUseCase.changeTaskState(input)
        }
    }
    @Test
    fun `should Throw exception when there is no tasks found to change state`() {
        // Given
        val id: UUID = mockk()
        val projectId: UUID = mockk()
        every { tasksRepository.getAllTasks() } returns emptyList()
        val input = Task(taskID = 123, taskTitle = "Videos1", state = State(id, "TODO", projectId))
        // When & Then
        assertThrows<Exception> {
            tasksUseCase.changeTaskState(input)
        }
    }

    @Test
    fun `should Throw exception when taskToEdit has null ID`() {
        // Given
        val id: UUID = mockk()
        val projectId: UUID = mockk()
        val tasks: List<Task> = listOf(
            createTaskHelper(taskID = null, taskTitle = "Videos1", state = State(id, "TODO", projectId))
        )
        every { tasksRepository.getAllTasks() } returns tasks

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.changeTaskState(Task(taskID = 123, state = State(id, "Done", projectId)))
        }
    }

    // Delete all tasks test cases
    @Test
    fun `should return true when all tasks deleted successfully `() {
        // Given
        val tasks: List<Task> = listOf(
            createTaskHelper(taskID = 1234, taskTitle = "Videos1"),
            createTaskHelper(taskID = 12345, taskTitle = "Videos2")
        )
        every { tasksRepository.getAllTasks() } returns tasks

        val expected = true
        val isAdmin = true
        // When
        val result = tasksUseCase.deleteAllTasks(isAdmin)

        // Then
        assertEquals(result, expected)
    }
    @Test
    fun `should Throw exception when there is no tasks found `() {
        // Given
        every { tasksRepository.getAllTasks() } returns emptyList()
        val isAdmin = true
        // When & Then
        assertThrows<Exception> {
            tasksUseCase.deleteAllTasks(isAdmin)
        }
    }
    @Test
    fun `should return exception when user is not admin`() {
        // Given
        val tasks: List<Task> = listOf(
            createTaskHelper(taskID = 1234, taskTitle = "Videos1"),
            createTaskHelper(taskID = 12345, taskTitle = "Videos2")
        )
        every { tasksRepository.getAllTasks() } returns tasks
        val isAdmin = false
        // When & Then
        assertThrows<Exception> {
            tasksUseCase.deleteAllTasks(isAdmin)
        }
    }

    // Get task by ID
    @Test
    fun `should return task model when found task successfully `() {
        // Given
        val tasks: List<Task> = listOf(
            createTaskHelper(taskID = 1234, taskTitle = "Videos1"),
            createTaskHelper(taskID = 12345, taskTitle = "Videos2")
        )
        val input = 1234
        every { tasksRepository.getAllTasks() } returns tasks

        val expected = Task(taskID = 1234, taskTitle = "Videos1")

        // When
        val result = tasksUseCase.getTaskById(input)

        // Then
        assertEquals(result, expected)
    }
    @Test
    fun `should Throw exception when wanted task not found`() {
        // Given
        val tasks: List<Task> = listOf(
            createTaskHelper(taskID = 1234, taskTitle = "Videos1"),
            createTaskHelper(taskID = 12345, taskTitle = "Videos2")
        )
        every { tasksRepository.getAllTasks() } returns tasks
        val input = 456

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.getTaskById(input)
        }
    }
    @Test
    fun `should Throw exception when there is no tasks`() {
        // Given
        every { tasksRepository.getAllTasks() } returns emptyList()
        val input = 456

        // When & Then
        assertThrows<Exception> {
            tasksUseCase.getTaskById(input)
        }
    }
}
