package logic.usecases.user

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.User
import logic.models.UserRole
import logic.repositories.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.UnauthorizedAccessException
import java.util.*

class CreateUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var createUserUseCase: CreateUserUseCase

    private val adminRole = UserRole.ADMIN
    private val mateRole = UserRole.MATE

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        createUserUseCase = CreateUserUseCase(userRepository)
    }

    @Test
    fun `should throw UnauthorizedAccessException for mates`() {
        // Given
        val newUser = User(UUID.randomUUID(), "User2", "", UserRole.MATE, listOf())
        every { userRepository.add(newUser) } returns true

        // When & Then
        val exception = assertThrows<UnauthorizedAccessException> {
            createUserUseCase(mateRole, newUser)
        }
        assertEquals("Only admins can create users", exception.message)
    }

    @Test
    fun `should create user for admins`() {
        // Given
        val newUser = User(UUID.randomUUID(), "User2", "", UserRole.MATE, listOf())
        every { userRepository.add(newUser) } returns true

        // When
        val result = createUserUseCase(adminRole, newUser)

        // Then
        assertTrue(result)
        verify(exactly = 1) { userRepository.add(newUser) }
    }
}
