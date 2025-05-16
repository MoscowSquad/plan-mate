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
        val newUser = User(
            UUID.randomUUID(), "User2", UserRole.MATE, listOf(),
            taskIds = listOf()
        )
        every { userRepository.addUser(any(), any()) } returns true

        // When & Then
        assertThrows<UnauthorizedAccessException> {
            createUserUseCase.invoke(mateRole, newUser, "")
        }
    }

    @Test
    fun `should create user for admins`() {
        // Given
        val newUser = User(
            UUID.randomUUID(), "User2", UserRole.MATE, listOf(),
            taskIds = listOf()
        )
        every { userRepository.addUser(any(), any()) } returns true

        // When
        val result = createUserUseCase.invoke(adminRole, newUser, "")

        // Then
        assertTrue(result)
        verify(exactly = 1) { userRepository.addUser(any(), any()) }
    }
}
