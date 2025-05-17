package domain.usecases.user

import domain.models.User
import domain.models.User.UserRole
import domain.repositories.UserRepository
import domain.util.UnauthorizedAccessException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class GetAllUsersUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var getAllUsersUseCase: GetAllUsersUseCase

    private val adminRole = UserRole.ADMIN
    private val mateRole = UserRole.MATE
    private val user = User(
        UUID.randomUUID(), "User1", UserRole.MATE, listOf(),
        taskIds = listOf()
    )

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        getAllUsersUseCase = GetAllUsersUseCase(userRepository)
    }

    @Test
    fun `should throw UnauthorizedAccessException when mate tries to get all users`() = runBlocking {
        // Given
        coEvery { userRepository.getAllUsers() } returns listOf(user)

        // When & Then
        assertThrows<UnauthorizedAccessException> {
            runBlocking { getAllUsersUseCase(mateRole) }
        }

        coVerify(exactly = 0) { userRepository.getAllUsers() }
    }

    @Test
    fun `should return all users when admin requests them`() = runBlocking {
        // Given
        val expectedUsers = listOf(user)
        coEvery { userRepository.getAllUsers() } returns expectedUsers

        // When
        val result = getAllUsersUseCase(adminRole)

        // Then
        assertEquals(expectedUsers, result)
        coVerify(exactly = 1) { userRepository.getAllUsers() }
    }
}