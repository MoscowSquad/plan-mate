package logic.usecases.user

import io.mockk.mockk
import io.mockk.verify
import logic.models.User
import logic.models.UserRole
import logic.repositoies.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.UnauthorizedAccessException
import java.util.*

class AssignProjectToUserUseCaseTest {

    private val userRepository: UserRepository = mockk()
    private val assignProjectToUserUseCase = AssignProjectToUserUseCase(userRepository)

    private val adminRole = UserRole.ADMIN
    private val mateRole = UserRole.MATE
    private val user = User(UUID.randomUUID(), "User1", "", UserRole.MATE, listOf())
    private val projectId = UUID.randomUUID()


    @Test
    fun `assignProjectToUserUseCase throws UnauthorizedAccessException for mates`() {
        // Given
        userRepository.add(user)

        // When & Then
        val exception = assertThrows<UnauthorizedAccessException> {
            assignProjectToUserUseCase(mateRole, projectId, user.id)
        }
        assertEquals("Only admins can assign projects to users", exception.message)
    }


    @Test
    fun `assignProjectToUserUseCase assigns project for admins`() {
        // Given
        userRepository.add(user)

        // When
        val result = assignProjectToUserUseCase(adminRole, projectId, user.id)

        // Then
        assertTrue(result)
        verify(exactly = 1) { assignProjectToUserUseCase(adminRole, projectId, user.id) }
    }
}
