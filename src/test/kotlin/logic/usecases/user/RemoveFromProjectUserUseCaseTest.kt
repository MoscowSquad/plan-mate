package logic.usecases.user

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.User
import logic.models.UserRole
import logic.repositoies.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.UnauthorizedAccessException
import java.util.*

class RemoveFromProjectUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var removeFromProjectUserUseCase: RemoveFromProjectUserUseCase

    private val adminRole = UserRole.ADMIN
    private val mateRole = UserRole.MATE
    private val user = User(UUID.randomUUID(), "User1", "", UserRole.MATE, listOf())
    private val projectId = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        removeFromProjectUserUseCase = RemoveFromProjectUserUseCase(userRepository)
    }

    @Test
    fun `should throw UnauthorizedAccessException for mates`() {
        // Given
        every { userRepository.add(user) } returns true

        // When & Then
        val exception = assertThrows<UnauthorizedAccessException> {
            removeFromProjectUserUseCase(mateRole, projectId, user.id)
        }
        assertEquals("Only admins can revoke projects from users", exception.message)
    }

    @Test
    fun `should revoke project for mates when user is admin`() {
        // Given
        every { userRepository.add(user) } returns true
        every { userRepository.removeFromProject(projectId, user.id) } returns true

        // When
        val result = removeFromProjectUserUseCase(adminRole, projectId, user.id)

        // Then
        assertTrue(result)
        verify(exactly = 1) { userRepository.removeFromProject(projectId, user.id) }
    }
}
