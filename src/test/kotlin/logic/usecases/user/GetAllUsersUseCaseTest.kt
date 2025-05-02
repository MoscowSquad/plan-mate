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

class GetAllUsersUseCaseTest {

    private val userRepository: UserRepository = mockk()
    private val getAllUsersUseCase = GetAllUsersUseCase(userRepository)

    private val adminRole = UserRole.ADMIN
    private val mateRole = UserRole.MATE
    private val user = User(UUID.randomUUID(), "User1", "", UserRole.MATE, listOf())

    @Test
    fun `getAllUsersUseCase throws UnauthorizedAccessException for mates`() {
        // Given
        userRepository.add(user)

        // When & Then
        val exception = assertThrows<UnauthorizedAccessException> {
            getAllUsersUseCase(mateRole)
        }
        assertEquals("Only admins can access all users", exception.message)
    }

    @Test
    fun `getAllUsersUseCase returns all users for admins`() {
        // Given
        userRepository.add(user)

        // When
        val result = getAllUsersUseCase(adminRole)

        // Then
        assertTrue(result.isNotEmpty())
        verify(exactly = 1) { userRepository.getAll() }
    }
}
