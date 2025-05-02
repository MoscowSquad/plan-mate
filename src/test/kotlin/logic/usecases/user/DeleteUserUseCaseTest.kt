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

class DeleteUserUseCaseTest {

    private val userRepository: UserRepository = mockk()
    private val deleteUserUseCase = DeleteUserUseCase(userRepository)

    private val adminRole = UserRole.ADMIN
    private val mateRole = UserRole.MATE
    private val user = User(UUID.randomUUID(), "User1", "", UserRole.MATE, listOf())

    @Test
    fun `deleteUserUseCase throws UnauthorizedAccessException for mates`() {
        // Given
        userRepository.add(user)

        // When & Then
        val exception = assertThrows<UnauthorizedAccessException> {
            deleteUserUseCase(mateRole, user.id)
        }
        assertEquals("Only admins can delete users", exception.message)
    }

    @Test
    fun `deleteUserUseCase deletes user for admins`() {
        // Given
        userRepository.add(user)

        // When
        deleteUserUseCase(adminRole, user.id)

        // Then
        assertThrows<NoSuchElementException> {
            userRepository.getById(user.id)
        }
        verify(exactly = 1) { userRepository.delete(user.id) }
    }
}
