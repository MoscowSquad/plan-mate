package domain.usecases.user

import domain.models.User
import domain.models.User.UserRole
import domain.repositories.UserRepository
import domain.util.UnauthorizedAccessException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class DeleteUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var deleteUserUseCase: DeleteUserUseCase

    private val adminRole = UserRole.ADMIN
    private val mateRole = UserRole.MATE
    private val user = User(
        UUID.randomUUID(), "User1", UserRole.MATE, listOf(),
        taskIds = listOf()
    )

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        deleteUserUseCase = DeleteUserUseCase(userRepository)
    }

    @Test
    fun `should throw UnauthorizedAccessException for mates`() {
        // Given
        every { userRepository.addUser(any(), any()) } returns true

        // When & Then
        assertThrows<UnauthorizedAccessException> {
            deleteUserUseCase(mateRole, user.id)
        }
    }

    @Test
    fun `should throw exception when user is not exist`() {
        // Given
        every { userRepository.getAllUsers() } returns listOf(user)
        every { userRepository.deleteUser(user.id) } returns false

        // When & Then
        assertThrows<NoSuchElementException> {
            deleteUserUseCase(adminRole, user.id)
        }
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
