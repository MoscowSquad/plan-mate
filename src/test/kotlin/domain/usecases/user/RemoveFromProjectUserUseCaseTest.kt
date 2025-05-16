package domain.usecases.user

import domain.models.User
import domain.models.User.UserRole
import domain.repositories.UserRepository
import domain.util.UnauthorizedAccessException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class RemoveFromProjectUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var removeFromProjectUserUseCase: RemoveFromProjectUserUseCase

    private val adminRole = UserRole.ADMIN
    private val mateRole = UserRole.MATE
    private val user = User(
        UUID.randomUUID(), "User1", UserRole.MATE, listOf(),
        taskIds = listOf()
    )
    private val projectId = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        removeFromProjectUserUseCase = RemoveFromProjectUserUseCase(userRepository)
    }

    @Test
    fun `should throw UnauthorizedAccessException for mates`() {
        // Given
        every { userRepository.addUser(any(), any()) } returns true

        // When & Then
        assertThrows<UnauthorizedAccessException> {
            removeFromProjectUserUseCase(mateRole, projectId, user.id)
        }
    }

    @Test
    fun `should revoke project for mates when user is admin`() {
        // Given
        every { userRepository.addUser(any(), any()) } returns true
        every { userRepository.unassignUserFromProject(projectId, user.id) } returns true

        // When
        val result = removeFromProjectUserUseCase(adminRole, projectId, user.id)

        // Then
        assertTrue(result)
        verify(exactly = 1) { userRepository.unassignUserFromProject(projectId, user.id) }
    }
}
