package data.repositories

import com.google.common.truth.Truth.assertThat
import data.csv_data.datasource.TaskStateDataSource
import data.csv_data.mappers.toDto
import data.csv_data.mappers.toTaskState
import data.csv_data.repositories.TaskStateRepositoryImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.TaskState
import logic.util.NoStateExistException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class TaskStateRepositoryImplTest {

    private lateinit var dataSource: TaskStateDataSource
    private lateinit var repository: TaskStateRepositoryImpl
    private lateinit var testState1: TaskState
    private lateinit var testState2: TaskState
    private lateinit var testState3: TaskState
    private lateinit var projectId1: UUID
    private lateinit var projectId2: UUID

    @BeforeEach
    fun setUp() {
        dataSource = mockk(relaxed = true)
        every { dataSource.fetch() } returns emptyList()

        projectId1 = UUID.fromString("11111111-1111-1111-1111-111111111111")
        projectId2 = UUID.fromString("22222222-2222-2222-2222-222222222222")

        testState1 = TaskState(
            id = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"),
            name = "To Do",
            projectId = projectId1
        )
        testState2 = TaskState(
            id = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"),
            name = "In Progress",
            projectId = projectId1
        )
        testState3 = TaskState(
            id = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"),
            name = "Done",
            projectId = projectId2
        )

        repository = TaskStateRepositoryImpl(dataSource)
    }

    @Test
    fun `init should fetch states from data source`() {
        // Given
        val stateDtos = listOf(testState1.toDto(), testState2.toDto())
        every { dataSource.fetch() } returns stateDtos

        // When
        val repository = TaskStateRepositoryImpl(dataSource)

        // Then
        verify { dataSource.fetch() }
        assertThat(repository.states.size).isEqualTo(2)
        assertThat(repository.states).contains(testState1.toDto().toTaskState())
        assertThat(repository.states).contains(testState2.toDto().toTaskState())
    }

    @Test
    fun `getTaskStateById should return state when it exists`() {
        // Given
        repository.addTaskState(projectId1, testState1)

        // When
        val result = repository.getTaskStateById(testState1.id)

        // Then
        assertThat(result).isEqualTo(testState1)
    }

    @Test
    fun `getTaskStateById should throw NoStateExistException when state does not exist`() {
        // Given
        val nonExistingId = UUID.randomUUID()

        // When & Then
        assertThrows<NoStateExistException> {
            repository.getTaskStateById(nonExistingId)
        }
    }

    @Test
    fun `getTaskStateByProjectId should return states for given project`() {
        // Given
        repository.addTaskState(projectId1, testState1)
        repository.addTaskState(projectId1, testState2)
        repository.addTaskState(projectId2, testState3)

        // When
        val result = repository.getTaskStateByProjectId(projectId1)

        // Then
        assertThat(result).containsExactlyElementsIn(listOf(testState1, testState2))
    }

    @Test
    fun `getTaskStateByProjectId should throw NoStateExistException when no states exist for project`() {
        // Given
        val nonExistingProjectId = UUID.randomUUID()

        // When & Then
        assertThrows<NoStateExistException> {
            repository.getTaskStateByProjectId(nonExistingProjectId)
        }
    }

    @Test
    fun `updateTaskState should modify existing state`() {
        // Given
        repository.addTaskState(projectId1, testState1)
        val updatedState = TaskState(testState1.id, "Updated State", projectId1)

        // When
        val result = repository.updateTaskState(updatedState)

        // Then
        assertThat(result).isTrue()
        assertThat(repository.getTaskStateById(testState1.id)).isEqualTo(updatedState)
        verify(exactly = 2) { dataSource.save(any()) }
    }

    @Test
    fun `updateTaskState should return false when state does not exist`() {
        // Given
        val nonExistingState = TaskState(UUID.randomUUID(), "Non-existing", projectId1)

        // When
        val result = repository.updateTaskState(nonExistingState)

        // Then
        assertThat(result).isFalse()
        verify(exactly = 0) { dataSource.save(any()) }
    }

    @Test
    fun `addTaskState should store state in repository`() {
        // When
        val result = repository.addTaskState(projectId1, testState1)

        // Then
        assertThat(result).isTrue()
        assertThat(repository.getTaskStateById(testState1.id)).isEqualTo(testState1)
        verify { dataSource.save(any()) }
    }

    @Test
    fun `addTaskState should handle multiple states`() {
        // When
        repository.addTaskState(projectId1, testState1)
        repository.addTaskState(projectId1, testState2)

        // Then
        val states = repository.getTaskStateByProjectId(projectId1)
        assertThat(states).containsExactlyElementsIn(listOf(testState1, testState2))
        verify(exactly = 2) { dataSource.save(any()) }
    }

    @Test
    fun `deleteTaskState should remove state from repository`() {
        // Given
        repository.addTaskState(projectId1, testState1)
        repository.addTaskState(projectId1, testState2)

        // When
        val result = repository.deleteTaskState(projectId1, testState1.id)

        // Then
        assertThat(result).isTrue()
        assertThat(repository.getTaskStateByProjectId(projectId1)).containsExactlyElementsIn(listOf(testState2))
        verify(exactly = 3) { dataSource.save(any()) }
    }

    @Test
    fun `deleteTaskState should return false when state does not exist`() {
        // Given
        val nonExistingId = UUID.randomUUID()

        // When
        val result = repository.deleteTaskState(projectId1, nonExistingId)

        // Then
        assertThat(result).isFalse()
        verify(exactly = 0) { dataSource.save(any()) }
    }

    @Test
    fun `deleteTaskState should return false when project id does not match`() {
        // Given
        repository.addTaskState(projectId1, testState1)

        // When
        val result = repository.deleteTaskState(projectId2, testState1.id)

        // Then
        assertThat(result).isFalse()
        verify(exactly = 1) { dataSource.save(any()) } // Only from the initial addTaskState
    }

    @Test
    fun `deleteTaskState should only delete when both project and state ids match`() {
        // Given
        repository.addTaskState(projectId1, testState1)
        repository.addTaskState(projectId2, testState3)

        // When
        val result = repository.deleteTaskState(projectId2, testState3.id)

        // Then
        assertThat(result).isTrue()
        assertThrows<NoStateExistException> {
            repository.getTaskStateByProjectId(projectId2)
        }
        assertThat(repository.getTaskStateById(testState1.id)).isEqualTo(testState1)
        verify(exactly = 3) { dataSource.save(any()) } // Two adds + one delete
    }

    @Test
    fun `getTaskStateById should find state after multiple additions and updates`() {
        // Given
        repository.addTaskState(projectId1, testState1)
        repository.addTaskState(projectId1, testState2)
        repository.updateTaskState(TaskState(testState1.id, "Updated Name", projectId1))

        // When
        val result = repository.getTaskStateById(testState1.id)

        // Then
        assertThat(result.name).isEqualTo("Updated Name")
        assertThat(result.id).isEqualTo(testState1.id)
    }

    @Test
    fun `getTaskStateById should find correct state when multiple states have same project ID`() {
        // Given
        repository.addTaskState(projectId1, testState1)
        repository.addTaskState(projectId1, testState2)

        // When
        val result = repository.getTaskStateById(testState2.id)

        // Then
        assertThat(result).isEqualTo(testState2)
    }

    @Test
    fun `getTaskStateById should still throw exception after adding and removing a state`() {
        // Given
        repository.addTaskState(projectId1, testState1)
        repository.deleteTaskState(projectId1, testState1.id)

        // When & Then
        assertThrows<NoStateExistException> {
            repository.getTaskStateById(testState1.id)
        }
    }

    @Test
    fun `addTaskState should check for existing state ID before adding`() {
        // Given
        repository.addTaskState(projectId1, testState1)
        val stateWithDuplicateId = testState1.copy()

        val mockStates = mockk<MutableList<TaskState>>(relaxed = true)
        every { mockStates.add(any()) } returns false
        every { dataSource.save(any()) } returns Unit


        val field = repository.javaClass.getDeclaredField("states")
        field.isAccessible = true
        field.set(repository, mockStates)

        // When
        val result = repository.addTaskState(projectId2, stateWithDuplicateId)

        // Then
        assertThat(result).isFalse()
        verify(exactly = 1) { dataSource.save(any()) }
        verify(exactly = 1) { mockStates.add(any()) }
    }
}