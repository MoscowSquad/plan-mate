package data.repositories

import com.google.common.truth.Truth.assertThat
import data.csv_data.datasource.TaskDataSource
import data.csv_data.mappers.toDto
import data.csv_data.mappers.toTask
import data.csv_data.repositories.TasksRepositoryImpl
import domain.models.Task
import domain.util.TaskIsExist
import domain.util.TaskIsNotFoundException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class TasksRepositoryImplTest {

    private lateinit var dataSource: TaskDataSource
    private lateinit var repository: TasksRepositoryImpl
    private lateinit var testTask1: Task
    private lateinit var testTask2: Task
    private lateinit var testTask3: Task
    private lateinit var projectId1: UUID
    private lateinit var projectId2: UUID

    @BeforeEach
    fun setUp() {
        dataSource = mockk(relaxed = true)

        projectId1 = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        projectId2 = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb")

        testTask1 = Task(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            "Task 1",
            "Description 1",
            projectId1,
            UUID.randomUUID()
        )
        testTask2 = Task(
            UUID.fromString("22222222-2222-2222-2222-222222222222"),
            "Task 2",
            "Description 2",
            projectId1,
            UUID.randomUUID()
        )
        testTask3 = Task(
            UUID.fromString("33333333-3333-3333-3333-333333333333"),
            "Task 3",
            "Description 3",
            projectId2,
            UUID.randomUUID()
        )

        every { dataSource.fetch() } returns emptyList()

        repository = TasksRepositoryImpl(dataSource)
    }

    @Test
    fun `init should fetch tasks from data source`() {
        // Given
        val taskDtos = listOf(testTask1.toDto(), testTask2.toDto())
        every { dataSource.fetch() } returns taskDtos

        // When
        val repository = TasksRepositoryImpl(dataSource)

        // Then
        verify { dataSource.fetch() }
        assertThat(repository.tasks.size).isEqualTo(2)
        assertThat(repository.tasks).contains(testTask1.toDto().toTask())
        assertThat(repository.tasks).contains(testTask2.toDto().toTask())
    }

    @Test
    fun `getAllTasks should return empty list when repository is empty`() = runBlocking {
        // When
        val result = repository.getAllTasks()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getAllTasks should return all tasks in repository`(): Unit = runBlocking {
        // Given
        repository.addTask(testTask1)
        repository.addTask(testTask2)
        repository.addTask(testTask3)

        // When
        val result = repository.getAllTasks()

        // Then
        assertThat(result).containsExactly(testTask1, testTask2, testTask3)
    }

    @Test
    fun `getAllTasks should return a copy of the internal list`() = runBlocking {
        // Given
        repository.addTask(testTask1)

        // When
        val tasks = repository.getAllTasks()
        tasks.toMutableList().add(testTask2)

        // Then
        assertThat(repository.tasks).doesNotContain(testTask2)
    }

    @Test
    fun `addTask should store task in repository`() = runBlocking {
        // When
        val result = repository.addTask(testTask1)

        // Then
        assertThat(result).isTrue()
        assertThat(repository.tasks).contains(testTask1)
        verify { dataSource.save(any()) }
    }

    @Test
    fun `addTask should throw TaskIsExist when task with same ID already exists`() = runBlocking {
        // Given
        repository.addTask(testTask1)
        val duplicateTask = Task(testTask1.id, "Duplicate Task", "Duplicate Description", projectId1, UUID.randomUUID())

        // When & Then
        assertThrows<TaskIsExist> {
            repository.addTask(duplicateTask)
        }
        verify(exactly = 1) { dataSource.save(any()) }
    }

    @Test
    fun `editTask should modify existing task`() = runBlocking {
        // Given
        repository.addTask(testTask1)
        val updatedTask =
            Task(testTask1.id, "Updated Task", "Updated Description", testTask1.projectId, testTask1.stateId)

        // When
        val result = repository.editTask(updatedTask)

        // Then
        assertThat(result).isTrue()
        assertThat(repository.getTaskById(testTask1.id)).isEqualTo(updatedTask)
        verify(exactly = 2) { dataSource.save(any()) }
    }

    @Test
    fun `editTask should throw TaskIsNotFoundException when task does not exist`() = runBlocking {
        // Given
        val nonExistingTask = Task(UUID.randomUUID(), "Non-existing", "Description", projectId1, UUID.randomUUID())

        // When & Then
        assertThrows<TaskIsNotFoundException> {
            repository.editTask(nonExistingTask)
        }
        verify(exactly = 0) { dataSource.save(any()) }
    }

    @Test
    fun `deleteTask should remove task from repository`() = runBlocking {
        // Given
        repository.addTask(testTask1)
        repository.addTask(testTask2)

        // When
        val result = repository.deleteTask(testTask1.id)

        // Then
        assertThat(result).isTrue()
        assertThat(repository.tasks).doesNotContain(testTask1)
        assertThat(repository.tasks).contains(testTask2)
        verify(exactly = 3) { dataSource.save(any()) }
    }

    @Test
    fun `deleteTask should throw TaskIsNotFoundException when task does not exist`() = runBlocking {
        // Given
        val nonExistingId = UUID.randomUUID()

        // When & Then
        assertThrows<TaskIsNotFoundException> {
            repository.deleteTask(nonExistingId)
        }
        verify(exactly = 0) { dataSource.save(any()) }
    }

    @Test
    fun `getTaskById should return task when it exists`() = runBlocking {
        // Given
        repository.addTask(testTask1)
        repository.addTask(testTask2)

        // When
        val result = repository.getTaskById(testTask2.id)

        // Then
        assertThat(result).isEqualTo(testTask2)
    }

    @Test
    fun `getTaskById should throw TaskIsNotFoundException when task does not exist`(): Unit = runBlocking {
        // Given
        val nonExistingId = UUID.randomUUID()

        // When & Then
        assertThrows<TaskIsNotFoundException> {
            repository.getTaskById(nonExistingId)
        }
    }

    @Test
    fun `getTaskByProjectId should return tasks with matching project ID`(): Unit = runBlocking {
        // Given
        repository.addTask(testTask1)
        repository.addTask(testTask2)
        repository.addTask(testTask3)

        // When
        val result = repository.getTaskByProjectId(projectId1)

        // Then
        assertThat(result).containsExactly(testTask1, testTask2)
    }

    @Test
    fun `getTaskByProjectId should return empty list when no tasks match project ID`() = runBlocking {
        // Given
        repository.addTask(testTask1)
        repository.addTask(testTask2)
        val nonExistingProjectId = UUID.randomUUID()

        // When
        val result = repository.getTaskByProjectId(nonExistingProjectId)

        // Then
        assertThat(result).isEmpty()
    }
}