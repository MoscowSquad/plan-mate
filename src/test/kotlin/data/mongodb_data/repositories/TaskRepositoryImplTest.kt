package data.mongodb_data.repositories

import com.google.common.truth.Truth.assertThat
import data.data_source.AuditLogDataSource
import data.data_source.TaskDataSource
import data.mongodb_data.dto.TaskDto
import data.mongodb_data.mappers.toDto
import data.mongodb_data.mappers.toTask
import domain.models.Task
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class TaskRepositoryImplTest {

    private lateinit var taskDataSource: TaskDataSource
    private lateinit var auditLogDataSource: AuditLogDataSource
    private lateinit var repository: TaskRepositoryImpl
    private lateinit var testTask1: Task
    private lateinit var testTask2: Task
    private lateinit var testTask3: Task
    private lateinit var projectId1: UUID
    private lateinit var projectId2: UUID
    private lateinit var testTaskDto1: TaskDto
    private lateinit var testTaskDto2: TaskDto
    private lateinit var testTaskDto3: TaskDto

    @BeforeEach
    fun setUp() {
        taskDataSource = mockk(relaxed = true)
        auditLogDataSource = mockk(relaxed = true)

        projectId1 = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        projectId2 = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb")

        testTask1 = Task(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            "Task 1",
            "Description 1",
            projectId1,
            UUID.randomUUID(),
            listOf()
        )
        testTask2 = Task(
            UUID.fromString("22222222-2222-2222-2222-222222222222"),
            "Task 2",
            "Description 2",
            projectId1,
            UUID.randomUUID(),
            listOf()
        )
        testTask3 = Task(
            UUID.fromString("33333333-3333-3333-3333-333333333333"),
            "Task 3",
            "Description 3",
            projectId2,
            UUID.randomUUID(),
            listOf()
        )

        testTaskDto1 = testTask1.toDto()
        testTaskDto2 = testTask2.toDto()
        testTaskDto3 = testTask3.toDto()

        repository = TaskRepositoryImpl(taskDataSource, auditLogDataSource)
    }

    @Test
    fun `getAllTasks should return all tasks from data source`() = runTest {
        // Given
        val taskDtos = listOf(testTaskDto1, testTaskDto2, testTaskDto3)
        coEvery { taskDataSource.getAllTasks() } returns taskDtos

        // When
        val result = repository.getAllTasks()

        // Then
        coVerify { taskDataSource.getAllTasks() }
        assertThat(result.size).isEqualTo(3)
        assertThat(result).contains(testTaskDto1.toTask())
        assertThat(result).contains(testTaskDto2.toTask())
        assertThat(result).contains(testTaskDto3.toTask())
    }

    @Test
    fun `getAllTasks should return empty list when data source is empty`() = runTest {
        // Given
        coEvery { taskDataSource.getAllTasks() } returns emptyList()

        // When
        val result = repository.getAllTasks()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTaskById should return task from data source`() = runTest {
        // Given
        coEvery { taskDataSource.getTaskById(testTask1.id) } returns testTaskDto1

        // When
        val result = repository.getTaskById(testTask1.id)

        // Then
        assertThat(result).isEqualTo(testTaskDto1.toTask())
        coVerify { taskDataSource.getTaskById(testTask1.id) }
    }

    @Test
    fun `getTaskByProjectId should return tasks with matching project ID`() = runTest {
        // Given
        val taskDtos = listOf(testTaskDto1, testTaskDto2)
        coEvery { taskDataSource.getTaskByProjectId(projectId1) } returns taskDtos

        // When
        val result = repository.getTaskByProjectId(projectId1)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result).contains(testTaskDto1.toTask())
        assertThat(result).contains(testTaskDto2.toTask())
        coVerify { taskDataSource.getTaskByProjectId(projectId1) }
    }

    @Test
    fun `getTaskByProjectId should return empty list when no tasks match project ID`() = runTest {
        // Given
        val nonExistingProjectId = UUID.randomUUID()
        coEvery { taskDataSource.getTaskByProjectId(nonExistingProjectId) } returns emptyList()

        // When
        val result = repository.getTaskByProjectId(nonExistingProjectId)

        // Then
        assertThat(result).isEmpty()
        coVerify { taskDataSource.getTaskByProjectId(nonExistingProjectId) }
    }
}