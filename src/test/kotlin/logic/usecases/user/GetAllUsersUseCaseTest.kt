package logic.usecases.user

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.User
import logic.models.User.UserRole
import logic.repositories.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import logic.util.UnauthorizedAccessException
import java.util.*

class GetAllUsersUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var getAllUsersUseCase: GetAllUsersUseCase

    private val adminRole = UserRole.ADMIN
    private val mateRole = UserRole.MATE
    private val user = User(UUID.randomUUID(), "User1", "", UserRole.MATE, listOf())

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        getAllUsersUseCase = GetAllUsersUseCase(userRepository)
    }

    @Test
    fun `should throw UnauthorizedAccessException for mates`() {
        // Given
        every { userRepository.addUser(user) } returns true

        // When & Then
        assertThrows<UnauthorizedAccessException> {
            getAllUsersUseCase(mateRole)
        }
    }

    @Test
    fun `should return all users for admins`() {
        // Given
        every { userRepository.addUser(user) } returns true
        every { userRepository.getAllUsers() } returns listOf(user)

        // When
        val result = getAllUsersUseCase(adminRole)

        // Then
        assertTrue(result.isNotEmpty())
        verify(exactly = 1) { userRepository.getAllUsers() }
    }
}
