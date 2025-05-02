package logic.usecases.user

import io.mockk.mockk
import io.mockk.verify
import logic.models.User
import logic.models.UserRole
import logic.repositoies.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class GetUserByIdUseCaseTest {

    private val userRepository: UserRepository = mockk()
    private val getUserByIdUseCase = GetUserByIdUseCase(userRepository)

    private val user = User(UUID.randomUUID(), "User1", "", UserRole.MATE, listOf())

    @Test
    fun `getUserByIdUseCase returns user by id`() {
        // Given
        userRepository.add(user)

        // When
        val result = getUserByIdUseCase(user.id)

        // Then
        assertEquals(user.id, result.id)
        verify(exactly = 1) { userRepository.getById(user.id) }
    }

    @Test
    fun `getUserByIdUseCase throws NoSuchElementException when user not found`() {
        // Given
        val nonExistentUserId = UUID.randomUUID()

        // When & Then
        val exception = assertThrows<NoSuchElementException> {
            getUserByIdUseCase(nonExistentUserId)
        }
        assertEquals("User with id $nonExistentUserId not found", exception.message)
    }
}
