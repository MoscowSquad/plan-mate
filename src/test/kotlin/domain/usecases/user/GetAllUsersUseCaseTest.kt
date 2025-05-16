package domain.usecases.user

import domain.models.User
import domain.models.User.UserRole
import domain.repositories.UserRepository
import domain.util.UnauthorizedAccessException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertTrue
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
        userRepository = mockk(relaxed = true)
        getAllUsersUseCase = GetAllUsersUseCase(userRepository)
    }

    @Test
    fun `should throw UnauthorizedAccessException for mates`() {
        // Given
        every { userRepository.addUser(any(), any()) } returns true

        // When & Then
        assertThrows<UnauthorizedAccessException> {
            getAllUsersUseCase(mateRole)
        }
    }

    @Test
    fun `should return all users for admins`() {
        // Given
        every { userRepository.addUser(any(), any()) } returns true
        every { userRepository.getAllUsers() } returns listOf(user)

        // When
        val result = getAllUsersUseCase(adminRole)

        // Then
        assertTrue(result.isNotEmpty())
        verify(exactly = 1) { userRepository.getAllUsers() }
    }
}
