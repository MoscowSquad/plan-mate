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

class CreateUserUseCaseTest {

    private val userRepository: UserRepository = mockk()
    private val createUserUseCase = CreateUserUseCase(userRepository)

    private val adminRole = UserRole.ADMIN
    private val mateRole = UserRole.MATE
    private val user = User(UUID.randomUUID(), "User1", "", UserRole.MATE, listOf())

    @Test
    fun `createUserUseCase throws UnauthorizedAccessException for mates`() {
        // Given
        val newUser = User(UUID.randomUUID(), "User2", "", UserRole.MATE, listOf())

        // When & Then
        val exception = assertThrows<UnauthorizedAccessException> {
            createUserUseCase(mateRole, newUser)
        }
        assertEquals("Only admins can create users", exception.message)
    }

    @Test
    fun `createUserUseCase creates user for admins`() {
        // Given
        val newUser = User(UUID.randomUUID(), "User2", "", UserRole.MATE, listOf())

        // When
        val result = createUserUseCase(adminRole, newUser)

        // Then
        assertTrue(result)
        verify(exactly = 1) { userRepository.add(newUser) }
    }
}
