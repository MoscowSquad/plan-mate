package domain.usecases.user

import domain.models.User
import domain.models.User.UserRole
import domain.repositories.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class GetUserByIdUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var getUserByIdUseCase: GetUserByIdUseCase

    private val user = User(
        UUID.randomUUID(), "User1", UserRole.MATE, listOf(),
        taskIds = listOf()
    )

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        getUserByIdUseCase = GetUserByIdUseCase(userRepository)
    }

    @Test
    fun `should return user by id when user exists`() {
        // Given
        every { userRepository.getUserById(user.id) } returns user

        // When
        val result = getUserByIdUseCase(user.id)

        // Then
        assertEquals(user.id, result.id)
        verify(exactly = 1) { userRepository.getUserById(user.id) }
    }

    @Test
    fun `should throw NoSuchElementException when user not found`() {
        // Given
        val nonExistentUserId = UUID.randomUUID()
        every { userRepository.getUserById(nonExistentUserId) } throws NoSuchElementException("User with id $nonExistentUserId not found")

        // When & Then
        assertThrows<NoSuchElementException> {
            getUserByIdUseCase(nonExistentUserId)
        }
    }
}
