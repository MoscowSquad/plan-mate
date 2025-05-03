package logic.usecases.user

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.User
import logic.models.UserRole
import logic.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import logic.util.UnauthorizedAccessException
import java.util.*

class AssignProjectToUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var assignProjectToUserUseCase: AssignProjectToUserUseCase

    private val adminRole = UserRole.ADMIN
    private val mateRole = UserRole.MATE
    private val user = User(UUID.randomUUID(), "User1", "", UserRole.MATE, listOf())
    private val projectId = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        assignProjectToUserUseCase = AssignProjectToUserUseCase(userRepository)
    }

    @Test
    fun `should throw UnauthorizedAccessException for mates try to assign project`() {
        // Given
        every { userRepository.addUser(user) } returns true

        // When & Then
        val exception = assertThrows<UnauthorizedAccessException> {
            assignProjectToUserUseCase(mateRole, projectId, user.id)
        }
        assertEquals("Only admins can assign projects to users", exception.message)
    }


    @Test
    fun `should assign project for users when admins try to assign`() {
        // Given
        every { userRepository.addUser(user) } returns true
        every { userRepository.assignUserToProject(projectId, user.id) } returns true

        // When
        val result = assignProjectToUserUseCase(adminRole, projectId, user.id)

        // Then
        assertTrue(result)
        verify(exactly = 1) { assignProjectToUserUseCase(adminRole, projectId, user.id) }
    }
}
