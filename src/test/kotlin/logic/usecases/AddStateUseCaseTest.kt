package logic.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.State
import logic.repositoies.ProjectsRepository
import logic.repositoies.StateRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.NoExistProjectException
import java.util.*

class AddStateUseCaseTest {
    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var stateRepository: StateRepository
    private lateinit var addStateUseCase: AddStateUseCase

    @BeforeEach
    fun setup() {
        projectsRepository = mockk<ProjectsRepository>()
        stateRepository = mockk<StateRepository>()
        addStateUseCase = AddStateUseCase(projectsRepository)
    }

    @Test
    fun `should return true when project exists`() {
        // Given
        val validProjectId = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val state = State(UUID.randomUUID(), "TODO", validProjectId)

        every { projectsRepository.isExist(validProjectId) } returns true

        // When
        val result = addStateUseCase(state)

        // Then
        assertTrue(result)
        verify { projectsRepository.isExist(validProjectId) }
    }

    @Test
    fun `should throw NoExistProjectException when project doesn't exist`() {
        // Given
        val projectId = UUID.fromString("00000000-0000-0000-0000-000000000000")
        val state = State(UUID.randomUUID(), "TODO", projectId)

        every { projectsRepository.isExist(any()) } throws NoExistProjectException(projectId)

        // When & Then
        assertThrows<NoExistProjectException> {
            addStateUseCase(state)
        }
        verify { projectsRepository.isExist(projectId) }
    }

    @Test
    fun `should verify repository call exactly once`() {
        // Given
        val projectId = UUID.fromString("00000000-0000-0000-0000-000000000003")
        val stateId =  UUID.fromString("00000000-0000-0000-0000-000000000002")
        val state = State(stateId, "TODO", projectId)

        every { projectsRepository.isExist(any()) } returns true

        // When
        addStateUseCase(state)

        // Then
        verify(exactly = 1) { projectsRepository.isExist(projectId) }
    }


    @Test
    fun `should return false when state title is blank(empty)`() {
        // Given
        val projectId = UUID.randomUUID()
        val inValidStates = listOf(
            State(UUID.randomUUID(), "", projectId), State(UUID.randomUUID(), "   ", projectId)
        )

        every { projectsRepository.isExist(any()) } returns true

        // When/Then
        inValidStates.forEach { state ->
            assertFalse(addStateUseCase(state))
        }
        verify(exactly = 0) { projectsRepository.isExist(any()) }
    }

}