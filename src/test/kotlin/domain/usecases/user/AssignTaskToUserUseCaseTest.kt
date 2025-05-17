package domain.usecases.user

import domain.models.User.UserRole
import domain.repositories.UserRepository
import domain.util.UnauthorizedAccessException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class AssignTaskToUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var assignTaskToUserUseCase: AssignTaskToUserUseCase

    private val adminRole = UserRole.ADMIN
    private val mateRole = UserRole.MATE
    private val userId = UUID.randomUUID()
    private val taskId = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        assignTaskToUserUseCase = AssignTaskToUserUseCase(userRepository)
    }

    @Test
    fun `should assign task to user when admin tries to assign`() = runBlocking {
        // Given
        coEvery { userRepository.assignUserToTask(taskId, userId) } returns true

        // When
        val result = assignTaskToUserUseCase(adminRole, taskId, userId)

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { userRepository.assignUserToTask(taskId, userId) }
    }

    @Test
    fun `should throw UnauthorizedAccessException when mate tries to assign task`(): Unit = runBlocking {
        // Given

        // When & Then
        assertThrows<UnauthorizedAccessException> {
            runBlocking { assignTaskToUserUseCase(mateRole, taskId, userId) }
        }

        coVerify(exactly = 0) { userRepository.assignUserToTask(any(), any()) }
    }

    @Test
    fun `should propagate repository result for admin role`() = runBlocking {
        // Given
        coEvery { userRepository.assignUserToTask(taskId, userId) } returns false

        // When
        val result = assignTaskToUserUseCase(adminRole, taskId, userId)

        // Then
        assertFalse(result)
        coVerify(exactly = 1) { userRepository.assignUserToTask(taskId, userId) }
    }
}