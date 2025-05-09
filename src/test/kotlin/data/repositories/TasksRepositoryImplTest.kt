package data.repositories

import com.google.common.truth.Truth.assertThat
import data.csv_parser.CsvHandler
import data.csv_parser.TaskCsvParser
import data.datasource.TaskDataSource
import data.mappers.toDto
import io.mockk.mockk
import logic.models.Task
import logic.util.TaskIsExist
import logic.util.TaskIsNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class TasksRepositoryImplTest {

    private lateinit var csvHandler: CsvHandler
    private lateinit var csvParser: TaskCsvParser
    private lateinit var dataSource: TaskDataSource
    private lateinit var tasksRepository: TasksRepositoryImpl
    private lateinit var testTask1: Task
    private lateinit var testTask2: Task
    private lateinit var testTask3: Task
    private lateinit var projectId1: UUID
    private lateinit var projectId2: UUID

    @BeforeEach
    fun setUp() {
        csvHandler = mockk(relaxed = true)
        csvParser = mockk(relaxed = true)
        dataSource = TaskDataSource(csvHandler, csvParser)
        tasksRepository = TasksRepositoryImpl(dataSource)

        projectId1 = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        projectId2 = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb")

        testTask1 = Task(UUID.fromString("11111111-1111-1111-1111-111111111111"), "Task 1", "Description 1", projectId1, UUID.randomUUID())
        testTask2 = Task(UUID.fromString("22222222-2222-2222-2222-222222222222"), "Task 2", "Description 2", projectId1, UUID.randomUUID())
        testTask3 = Task(UUID.fromString("33333333-3333-3333-3333-333333333333"), "Task 3", "Description 3", projectId2, UUID.randomUUID())
    }

    @Test
    fun `getAllTasks should return empty list when repository is empty`() {
        // When
        val result = tasksRepository.getAllTasks()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getAllTasks should return all tasks in repository`() {
        // Given
        tasksRepository.addTask(testTask1)
        tasksRepository.addTask(testTask2)
        tasksRepository.addTask(testTask3)

        // When
        val result = tasksRepository.getAllTasks()

        // Then
        assertThat(result).containsExactly(testTask1, testTask2, testTask3)
    }

    @Test
    fun `getAllTasks should return a copy of the internal list`() {
        // Given
        tasksRepository.addTask(testTask1)

        // When
        val tasks = tasksRepository.getAllTasks()
        val mutableTasks = tasks.toMutableList()
        mutableTasks.add(testTask2)

        // Then
        assertThat(tasksRepository.getAllTasks()).containsExactly(testTask1)
    }

    @Test
    fun `addTask should store task in repository`() {
        // When
        val result = tasksRepository.addTask(testTask1)

        // Then
        assertThat(result).isTrue()
        assertThat(tasksRepository.getAllTasks()).containsExactly(testTask1)
    }

    @Test
    fun `addTask should throw TaskIsExist when task with same ID already exists`() {
        // Given
        tasksRepository.addTask(testTask1)
        val duplicateTask = Task(testTask1.id, "Duplicate Task", "Duplicate Description", projectId1, UUID.randomUUID())
        // When & Then
        val exception = assertThrows<TaskIsExist> {
            tasksRepository.addTask(duplicateTask)
        }

        assertThat(exception.message).contains(testTask1.id.toString())
    }

    @Test
    fun `editTask should modify existing task`() {
        // Given
        tasksRepository.addTask(testTask1)
        val updatedTask = Task(testTask1.id, "Updated Task", "Updated Description", testTask1.projectId, testTask1.stateId)
        // When
        val result = tasksRepository.editTask(updatedTask)

        // Then
        assertThat(result).isTrue()
        assertThat(tasksRepository.getTaskById(testTask1.id)).isEqualTo(updatedTask)
    }

    @Test
    fun `editTask should throw TaskIsNotFoundException when task does not exist`() {
        // Given
        val nonExistingTask = Task(UUID.randomUUID(), "Non-existing", "Description", projectId1, UUID.randomUUID())
        // When & Then
        val exception = assertThrows<TaskIsNotFoundException> {
            tasksRepository.editTask(nonExistingTask)
        }

        assertThat(exception.message).contains(nonExistingTask.id.toString())
    }

    @Test
    fun `deleteTask should remove task from repository`() {
        // Given
        tasksRepository.addTask(testTask1)
        tasksRepository.addTask(testTask2)

        // When
        val result = tasksRepository.deleteTask(testTask1.id)

        // Then
        assertThat(result).isTrue()
        assertThat(tasksRepository.getAllTasks()).containsExactly(testTask2)
    }

    @Test
    fun `deleteTask should throw TaskIsNotFoundException when task does not exist`() {
        // Given
        val nonExistingId = UUID.randomUUID()

        // When & Then
        val exception = assertThrows<TaskIsNotFoundException> {
            tasksRepository.deleteTask(nonExistingId)
        }

        assertThat(exception.message).contains(nonExistingId.toString())
    }

    @Test
    fun `getTaskById should return task when it exists`() {
        // Given
        tasksRepository.addTask(testTask1)
        tasksRepository.addTask(testTask2)

        // When
        val result = tasksRepository.getTaskById(testTask2.id)

        // Then
        assertThat(result).isEqualTo(testTask2)
    }

    @Test
    fun `getTaskById should throw TaskIsNotFoundException when task does not exist`() {
        // Given
        val nonExistingId = UUID.randomUUID()

        // When & Then
        val exception = assertThrows<TaskIsNotFoundException> {
            tasksRepository.getTaskById(nonExistingId)
        }

        assertThat(exception.message).contains(nonExistingId.toString())
    }

    @Test
    fun `getTaskByProjectId should return tasks with matching project ID`() {
        // Given
        tasksRepository.addTask(testTask1)
        tasksRepository.addTask(testTask2)
        tasksRepository.addTask(testTask3)

        // When
        val result = tasksRepository.getTaskByProjectId(projectId1)

        // Then
        assertThat(result).containsExactly(testTask1, testTask2)
    }

    @Test
    fun `getTaskByProjectId should return empty list when no tasks match project ID`() {
        // Given
        tasksRepository.addTask(testTask1)
        tasksRepository.addTask(testTask2)
        val nonExistingProjectId = UUID.randomUUID()

        // When
        val result = tasksRepository.getTaskByProjectId(nonExistingProjectId)

        // Then
        assertThat(result).isEmpty()
    }
    @Test
    fun `init block should load tasks from data source`() {
        // Given
        val task1 = Task(UUID.randomUUID(), "Task 1", "Description 1", projectId1, UUID.randomUUID())
        val task2 = Task(UUID.randomUUID(), "Task 2", "Description 2", projectId2, UUID.randomUUID())
        val taskDtos = listOf(task1.toDto(), task2.toDto())

        io.mockk.clearAllMocks()

        val testDataSource = mockk<TaskDataSource>(relaxed = false)
        io.mockk.every { testDataSource.fetch() } returns taskDtos

        val repository = TasksRepositoryImpl(testDataSource)

        val allTasks = repository.getAllTasks()
        assertThat(allTasks).hasSize(2)
        assertThat(allTasks[0].id).isEqualTo(task1.id)
        assertThat(allTasks[1].id).isEqualTo(task2.id)
        io.mockk.verify { testDataSource.fetch() }
    }
}