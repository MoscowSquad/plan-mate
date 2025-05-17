package domain.usecases.user

import domain.models.User.UserRole
import domain.repositories.UserRepository
import domain.util.UnauthorizedAccessException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class DeleteUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var deleteUserUseCase: DeleteUserUseCase

    private val adminRole = UserRole.ADMIN
    private val mateRole = UserRole.MATE
    private val userId = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        deleteUserUseCase = DeleteUserUseCase(userRepository)
    }

    @Test
    fun `should throw UnauthorizedAccessException when mate tries to delete user`() = runBlocking {
        // Given
        coEvery { userRepository.deleteUser(any()) } returns true

        // When & Then
        assertThrows<UnauthorizedAccessException> {
            runBlocking { deleteUserUseCase(mateRole, userId) }
        }

        coVerify(exactly = 0) { userRepository.deleteUser(any()) }
    }

    @Test
    fun `should throw NoSuchElementException when user does not exist`() = runBlocking {
        // Given
        coEvery { userRepository.deleteUser(userId) } returns false

        // When & Then
        assertThrows<NoSuchElementException> {
            runBlocking { deleteUserUseCase(adminRole, userId) }
        }

        coVerify(exactly = 1) { userRepository.deleteUser(userId) }
    }

    @Test
    fun `should delete user when admin tries to delete`() = runBlocking {
        // Given
        coEvery { userRepository.deleteUser(userId) } returns true

        // When
        deleteUserUseCase(adminRole, userId)

        // Then
        coVerify(exactly = 1) { userRepository.deleteUser(userId) }
    }
}