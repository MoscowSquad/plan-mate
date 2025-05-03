package logic.usecases.task_state

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.TaskState
import logic.repositories.TaskStateRepository
import logic.util.IllegalStateTitle
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*


class EditTaskStateUseCaseTest {
    private lateinit var editStateUseCase: EditTaskStateUseCase
    private lateinit var stateRepository: TaskStateRepository

    @BeforeEach
    fun setup() {
        stateRepository = mockk()
        editStateUseCase = EditTaskStateUseCase(stateRepository)
    }

    @Test
    fun `when valid title should return updated TaskState `() {
        // Given
        val originalState = TaskState(
            id = UUID.fromString("00000000-0000-0000-0000-000000000002"),
            projectId = UUID.fromString("00000000-0000-0000-0000-000000000001"),
            name = "Old TaskState"
        )
        val newTask = TaskState(originalState.id, "Updated TaskState", originalState.projectId)

        every { stateRepository.updateTaskState(any()) } returns true

        // When
        val result = editStateUseCase(originalState)

        // Then
        assertThat(result).isEqualTo(
            newTask
        )

        verify { stateRepository.updateTaskState(newTask) }
    }

    @Test
    fun `when title is blank should throw IllegalStateTitle`() {
        // Given
        val invalidState = TaskState(
            id = UUID.randomUUID(),
            name = "",
            projectId = UUID.randomUUID()
        )

        // When & Then
        val exception = assertThrows<IllegalStateTitle> {
            editStateUseCase(invalidState)
        }
        assertThat("TaskState title cannot be blank").isEqualTo(exception)

    }


}

