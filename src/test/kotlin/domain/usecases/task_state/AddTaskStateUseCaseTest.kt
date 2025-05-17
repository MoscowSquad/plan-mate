package domain.usecases.task_state

import domain.models.TaskState
import domain.repositories.TaskStateRepository
import domain.util.IllegalStateTitle
import domain.util.NotAdminException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
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
    fun `should return true when state is successfully added`() = runTest {
        // Given
        val validState = TaskState(
            id = UUID.randomUUID(),
            name = "Valid State",
            projectId = UUID.randomUUID()
        )

        coEvery { stateRepository.addTaskState(validState.projectId, validState) } returns true

        // When
        val result = addStateUseCase(validState, true)

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { stateRepository.addTaskState(validState.projectId, validState) }
    }

    @Test
    fun `should throw IllegalStateTitle when title is blank`() = runTest {
        // Given
        val invalidState = TaskState(
            id = UUID.randomUUID(),
            name = "",
            projectId = UUID.randomUUID()
        )

        // When & Then
        assertThrows<IllegalStateTitle> {
            runTest { addStateUseCase(invalidState, true) }
        }
        coVerify(exactly = 0) { stateRepository.addTaskState(any(), any()) }
    }

    @Test
    fun `should throw IllegalStateTitle when title is too long`() = runTest {
        // Given
        val longTitle = "a".repeat(101)
        val invalidState = TaskState(
            id = UUID.randomUUID(),
            name = longTitle,
            projectId = UUID.randomUUID()
        )

        // When & Then
        assertThrows<IllegalStateTitle> {
            runTest { addStateUseCase(invalidState, true) }
        }
        coVerify(exactly = 0) { stateRepository.addTaskState(any(), any()) }
    }

    @Test
    fun `should throw NotAdminException when user is not admin`() = runTest {
        // Given
        val validState = TaskState(
            id = UUID.randomUUID(),
            name = "Valid State",
            projectId = UUID.randomUUID()
        )

        // When & Then
        assertThrows<NotAdminException> {
            runTest { addStateUseCase(validState, false) }
        }
        coVerify(exactly = 0) { stateRepository.addTaskState(any(), any()) }
    }

    @Test
    fun `should throw when repository fails to add`() = runTest {
        // Given
        val validState = TaskState(
            id = UUID.randomUUID(),
            name = "Valid",
            projectId = UUID.randomUUID()
        )

        coEvery { stateRepository.addTaskState(validState.projectId, validState) } returns false

        // When & Then
        assertThrows<IllegalStateException> {
            runTest { addStateUseCase(validState, true) }
        }

        coVerify(exactly = 1) { stateRepository.addTaskState(validState.projectId, validState) }
    }

    @Test
    fun `should accept titles at boundary conditions`() = runTest {
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

        coEvery { stateRepository.addTaskState(any(), any()) } returns true

        // When & Then
        assertTrue(addStateUseCase(minLengthState, true))
        coVerify(exactly = 1) { stateRepository.addTaskState(minLengthState.projectId, minLengthState) }

        assertTrue(addStateUseCase(maxLengthState, true))
        coVerify(exactly = 1) { stateRepository.addTaskState(maxLengthState.projectId, maxLengthState) }
    }
}