package logic.usecases.task_state

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.TaskState
import logic.repositories.TaskStateRepository
import logic.util.IllegalStateTitle
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class AddTaskStateUseCaseTest {
    private lateinit var stateRepository: TaskStateRepository

    private lateinit var addStateUseCase: AddTaskStateUseCase

    @BeforeEach
    fun setUp() {
        stateRepository = mockk()
        addStateUseCase = AddTaskStateUseCase(stateRepository)
    }

    @Test
    fun `should return true when state is successfully added`() {
        // Given
        val validState = TaskState(
            id = UUID.randomUUID(),
            name = "Valid State",
            projectId = UUID.randomUUID()
        )

        every { stateRepository.addTaskState(validState.projectId, validState) } returns true

        // When
        val result = addStateUseCase(validState, true)

        // Then
        assertTrue(result)
        verify(exactly = 1) { stateRepository.addTaskState(validState.projectId, validState) }
    }

    @Test
    fun `should throw IllegalStateTitle when title is blank`() {
        // Given
        val invalidState = TaskState(
            id = UUID.randomUUID(),
            name = "",
            projectId = UUID.randomUUID()
        )

        // When & Then
        assertThrows<IllegalStateTitle> {
            addStateUseCase(invalidState, true)
        }
        verify(exactly = 0) { stateRepository.addTaskState(any(), any()) }
    }

    @Test
    fun `should throw IllegalStateTitle when title is too long`() {
        // Given
        val longTitle = "a".repeat(101)
        val invalidState = TaskState(
            id = UUID.randomUUID(),
            name = longTitle,
            projectId = UUID.randomUUID()
        )

        // When & Then
        assertThrows<IllegalStateTitle> {
            addStateUseCase(invalidState, true)
        }
        verify(exactly = 0) { stateRepository.addTaskState(any(), any()) }
    }

    @Test
    fun `should throw when repository fails to add`() {
        // Given
        val validState = TaskState(
            id = UUID.randomUUID(),
            name = "Valid",
            projectId = UUID.randomUUID()
        )

        every { stateRepository.addTaskState(validState.projectId, validState) } returns false

        // When & Then
        assertThrows<IllegalStateException> {
            addStateUseCase(validState, true)
        }

        verify(exactly = 1) { stateRepository.addTaskState(validState.projectId, validState) }
    }

    @Test
    fun `should accept titles at boundary conditions`() {
        // Given
        val minLengthState = TaskState(
            id = UUID.randomUUID(),
            name = "a",
            projectId = UUID.randomUUID()
        )

        val maxLengthState = TaskState(
            id = UUID.randomUUID(),
            name = "a".repeat(100),
            projectId = UUID.randomUUID()
        )

        every { stateRepository.addTaskState(any(), any()) } returns true

        // When & Then

        assertTrue(addStateUseCase(minLengthState, true))
        verify(exactly = 1) { stateRepository.addTaskState(minLengthState.projectId, minLengthState) }


        assertTrue(addStateUseCase(maxLengthState, true))
        verify(exactly = 1) { stateRepository.addTaskState(maxLengthState.projectId, maxLengthState) }
    }
}