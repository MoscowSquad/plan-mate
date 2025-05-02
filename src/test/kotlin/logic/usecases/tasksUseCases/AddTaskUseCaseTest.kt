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
import java.util.*

class AddTaskUseCaseTest {
    private lateinit var addTaskUseCase: AddTaskUseCase
    private lateinit var tasksRepository: TasksRepository
    val id = UUID.fromString("00000000-0000-0000-0000-000000000001")
    val id2: UUID = UUID.fromString("00000000-0000-0000-0000-000000000002")

    @BeforeEach
    fun setup() {
        tasksRepository = mockk(relaxed = true)
        addTaskUseCase = AddTaskUseCase(tasksRepository)
    }
//UUID.fromString("00000000-0000-0000-0000-000000000002")
    @Test
    fun `should return list of Tasks when add task without any issue`() {
        // Given
        val projectId: UUID = mockk()
        val tasks: List<Task> = listOf(
            Task(id=id, title = "Videos",projectId=id,stateId = id),
            Task(id=id2, title = "Videos2",projectId=id2,stateId = id2),
            )
        val task = Task(id = UUID.fromString("00000000-0000-0000-0000-000000000003")
            , title = "Videos3"  ,projectId=id2,stateId = id2)

        every { tasksRepository.getAll() } returns tasks

        val expected = listOf(
            Task(id=id, title = "Videos",projectId=id,stateId = id),
            Task(id=id2, title = "Videos2",projectId=id2,stateId = id2),
            task,
        )

        // When
        val result = addTaskUseCase.addTask(task)
        // Then
        assertEquals(result, expected)
    }
    @Test
    fun `should Throw PropertyNullException when  ID is null `() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id=null, title = "Videos",projectId=id,stateId = id),
            Task(id=id2, title = "Videos2",projectId=id2,stateId = id2),
        )
        every { tasksRepository.getAll() } returns tasks

        // When & Then
        assertThrows<PropertyNullException> {
            addTaskUseCase.addTask(Task(id = null
                , title = "Videos3"  ,projectId= UUID.fromString("00000000-0000-0000-0000-000000000003"),stateId = UUID.fromString("00000000-0000-0000-0000-000000000003")))
        }
    }
    @Test
    fun `should Throw PropertyNullException when user title is null`() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id=id, title = "Videos",projectId=id,stateId = id),
            Task(id=id2, title = "Videos2",projectId=id2,stateId = id2),
        )
        every { tasksRepository.getAll() } returns tasks

        // When & Then
        assertThrows<PropertyNullException> {
            addTaskUseCase.addTask(Task(id = UUID.fromString("00000000-0000-0000-0000-000000000003")
                , title = null  ,projectId=id2,stateId = id2))
        }
    }

    @Test
    fun `should Throw PropertyNullException when state ID is null `() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id=id, title = "Videos",projectId=id,stateId = null),
            Task(id=id2, title = "Videos2",projectId=id2,stateId = id2),
        )
        every { tasksRepository.getAll() } returns tasks

        // When & Then
        assertThrows<PropertyNullException> {
            addTaskUseCase.addTask(Task(id = UUID.fromString("00000000-0000-0000-0000-000000000003")
                , title = "Videos3"  ,projectId= UUID.fromString("00000000-0000-0000-0000-000000000003"),stateId = null))
        }
    }

}