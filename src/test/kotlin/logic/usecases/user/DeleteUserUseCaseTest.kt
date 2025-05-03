package logic.usecases.user

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.User
import logic.models.UserRole
import logic.repositories.UserRepository
import logic.util.UnauthorizedAccessException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class DeleteUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var deleteUserUseCase: DeleteUserUseCase

    private val adminRole = UserRole.ADMIN
    private val mateRole = UserRole.MATE
    private val user = User(UUID.randomUUID(), "User1", "", UserRole.MATE, listOf())

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        deleteUserUseCase = DeleteUserUseCase(userRepository)
    }

    @Test
    fun `should throw UnauthorizedAccessException for mates`() {
        // Given
        every { userRepository.addUser(user) } returns true

        // When & Then
        val exception = assertThrows<UnauthorizedAccessException> {
            deleteUserUseCase(mateRole, user.id)
        }
        assertEquals("Only admins can delete users", exception.message)
    }

    @Test
    fun `should throw exception when user is not exist`() {
        // Given
        every { userRepository.getAllUsers() } returns listOf(user)
        every { userRepository.deleteUser(user.id) } returns false

        // When & Then
        val exception = assertThrows<NoSuchElementException> {
            deleteUserUseCase(adminRole, user.id)
        }
        assertEquals("User with id ${user.id} not found", exception.message)
    }

    @Test
    fun `should delete user for admins`() {
        // Given
        every { userRepository.getAllUsers() } returns listOf(user)
        every { userRepository.deleteUser(user.id) } returns true

        // When
        deleteUserUseCase(adminRole, user.id)

        // Then
        verify(exactly = 1) { userRepository.deleteUser(user.id) }
    }
}
