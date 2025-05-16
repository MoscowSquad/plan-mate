package domain.usecases.user

import domain.models.User
import domain.repositories.UserRepository
import domain.util.UnauthorizedAccessException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class AssignTaskToUserUseCaseTest {

    private val mockUserRepository = mockk<UserRepository>()
    private val useCase = AssignTaskToUserUseCase(mockUserRepository)

    @Test
    fun `invoke should call repository when role is ADMIN`() {
        // Given
        val taskId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        every { mockUserRepository.assignUserToTask(taskId, userId) } returns true

        // When
        val result = useCase(User.UserRole.ADMIN, taskId, userId)

        // Then
        assertTrue(result)
        verify(exactly = 1) { mockUserRepository.assignUserToTask(taskId, userId) }
    }

    @Test
    fun `invoke should throw UnauthorizedAccessException for non-ADMIN roles`() {
        // Given
        val taskId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        // When & Then
        assertThrows<UnauthorizedAccessException> {
            useCase(User.UserRole.MATE, taskId, userId)
        }
        verify(exactly = 0) { mockUserRepository.assignUserToTask(any(), any()) }
    }

    @Test
    fun `invoke should propagate repository result for ADMIN role`() {
        // Given
        val taskId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        every { mockUserRepository.assignUserToTask(taskId, userId) } returns false

        // When
        val result = useCase(User.UserRole.ADMIN, taskId, userId)

        // Then
        assertFalse(result)
        verify(exactly = 1) { mockUserRepository.assignUserToTask(taskId, userId) }
    }
}