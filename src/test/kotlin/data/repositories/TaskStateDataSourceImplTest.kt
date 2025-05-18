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
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class TaskStateDataSourceImplTest {

    private lateinit var dataSource: TaskDataSource
    private lateinit var repository: TasksRepositoryImpl
    private lateinit var testTask1: Task
    private lateinit var testTask2: Task
    private lateinit var testTask3: Task
    private lateinit var projectId1: UUID
    private lateinit var projectId2: UUID
    private lateinit var stateId1: UUID
    private lateinit var stateId2: UUID

    @BeforeEach
    fun setUp() {
        dataSource = mockk(relaxed = true)
        every { dataSource.fetch() } returns emptyList()

        projectId1 = UUID.fromString("11111111-1111-1111-1111-111111111111")
        projectId2 = UUID.fromString("22222222-2222-2222-2222-222222222222")
        stateId1 = UUID.fromString("33333333-3333-3333-3333-333333333333")
        stateId2 = UUID.fromString("44444444-4444-4444-4444-444444444444")

        testTask1 = Task(
            id = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"),
            title = "Task 1",
            description = "Description 1",
            projectId = projectId1,
            stateId = stateId1
        )
        testTask2 = Task(
            id = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"),
            title = "Task 2",
            description = "Description 2",
            projectId = projectId1,
            stateId = stateId2
        )
        testTask3 = Task(
            id = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"),
            title = "Task 3",
            description = "Description 3",
            projectId = projectId2,
            stateId = stateId1
        )

        repository = TasksRepositoryImpl(dataSource)
    }

    @Test
    fun `init should fetch tasks from data source`() = runTest {
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
    fun `getAllTasks should return all tasks`(): Unit = runTest {
        // Given
        repository.addTask(testTask1)
        repository.addTask(testTask2)

        // When
        val result = repository.getAllTasks()

        // Then
        assertThat(result).containsExactly(testTask1, testTask2)
    }

    @Test
    fun `addTask should store task in repository`() = runTest {
        // When
        val result = repository.addTask(testTask1)

        // Then
        assertThat(result).isTrue()
        assertThat(repository.tasks).contains(testTask1)
        verify { dataSource.save(any()) }
    }

    @Test
    fun `addTask should throw TaskIsExist when task with same ID already exists`() = runTest {
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
    fun `editTask should modify existing task`() = runTest {
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
    fun `editTask should throw TaskIsNotFoundException when task does not exist`() = runTest {
        // Given
        val nonExistingTask = Task(UUID.randomUUID(), "Non-existing", "Description", projectId1, UUID.randomUUID())

        // When & Then
        assertThrows<TaskIsNotFoundException> {
            repository.editTask(nonExistingTask)
        }
        verify(exactly = 0) { dataSource.save(any()) }
    }

    @Test
    fun `deleteTask should remove task from repository`() = runTest {
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
    fun `deleteTask should throw TaskIsNotFoundException when task does not exist`() = runTest {
        // Given
        val nonExistingId = UUID.randomUUID()

        // When & Then
        assertThrows<TaskIsNotFoundException> {
            repository.deleteTask(nonExistingId)
        }
        verify(exactly = 0) { dataSource.save(any()) }
    }

    @Test
    fun `getTaskById should return task when it exists`() = runTest {
        // Given
        repository.addTask(testTask1)
        repository.addTask(testTask2)

        // When
        val result = repository.getTaskById(testTask2.id)

        // Then
        assertThat(result).isEqualTo(testTask2)
    }

    @Test
    fun `getTaskById should throw TaskIsNotFoundException when task does not exist`(): Unit = runTest {
        // Given
        val nonExistingId = UUID.randomUUID()

        // When & Then
        assertThrows<TaskIsNotFoundException> {
            repository.getTaskById(nonExistingId)
        }
    }

    @Test
    fun `getTaskByProjectId should return tasks with matching project ID`(): Unit = runTest {
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
    fun `getTaskByProjectId should return empty list when no tasks match project ID`() = runTest {
        // Given
        repository.addTask(testTask1)
        repository.addTask(testTask2)
        val nonExistingProjectId = UUID.randomUUID()

        // When
        val result = repository.getTaskByProjectId(nonExistingProjectId)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTaskById should find task after multiple additions and updates`() = runTest {
        // Given
        repository.addTask(testTask1)
        repository.addTask(testTask2)
        val updatedTask =
            Task(testTask1.id, "Updated Task", "Updated Description", testTask1.projectId, testTask1.stateId)
        repository.editTask(updatedTask)

        // When
        val result = repository.getTaskById(testTask1.id)

        // Then
        assertThat(result.title).isEqualTo("Updated Task")
        assertThat(result.description).isEqualTo("Updated Description")
        assertThat(result.id).isEqualTo(testTask1.id)
    }

    @Test
    fun `getTaskById should find correct task when multiple tasks have same project ID`() = runTest {
        // Given
        repository.addTask(testTask1)
        repository.addTask(testTask2)

        // When
        val result = repository.getTaskById(testTask2.id)

        // Then
        assertThat(result).isEqualTo(testTask2)
    }

    @Test
    fun `getTaskById should throw exception after adding and removing a task`(): Unit = runTest {
        // Given
        repository.addTask(testTask1)
        repository.deleteTask(testTask1.id)

        // When & Then
        assertThrows<TaskIsNotFoundException> {
            repository.getTaskById(testTask1.id)
        }
    }

    @Test
    fun `editing task should not affect other tasks`() = runTest {
        // Given
        repository.addTask(testTask1)
        repository.addTask(testTask2)
        val updatedTask =
            Task(testTask1.id, "Updated Task", "Updated Description", testTask1.projectId, testTask1.stateId)

        // When
        repository.editTask(updatedTask)

        // Then
        assertThat(repository.getTaskById(testTask1.id)).isEqualTo(updatedTask)
        assertThat(repository.getTaskById(testTask2.id)).isEqualTo(testTask2)
    }

    @Test
    fun `tasks with same project but different states should be returned by getTaskByProjectId`(): Unit = runTest {
        // Given
        repository.addTask(testTask1) // projectId1, stateId1
        repository.addTask(testTask2) // projectId1, stateId2

        // When
        val result = repository.getTaskByProjectId(projectId1)

        // Then
        assertThat(result).containsExactly(testTask1, testTask2)
    }
}