package domain.usecases.user

import domain.models.User
import domain.models.User.UserRole
import domain.repositories.UserRepository
import domain.util.UnauthorizedAccessException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
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
    fun `should throw UnauthorizedAccessException when mate tries to create user`() = runTest {
        // Given
        val newUser = User(
            UUID.randomUUID(), "User2", UserRole.MATE, listOf(),
            taskIds = listOf()
        )
        coEvery { userRepository.addUser(any(), any()) } returns true

        // When & Then
        assertThrows<UnauthorizedAccessException> {
             createUserUseCase(mateRole, newUser, "password")
        }

        coVerify(exactly = 0) { userRepository.addUser(any(), any()) }
    }

    @Test
    fun `should create user when admin tries to create`() = runTest {
        // Given
        val newUser = User(
            UUID.randomUUID(), "User2", UserRole.MATE, listOf(),
            taskIds = listOf()
        )
        coEvery { userRepository.addUser(any(), any()) } returns true

        // When
        val result = createUserUseCase(adminRole, newUser, "password")

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { userRepository.addUser(any(), any()) }
    }

    @Test
    fun `should propagate repository result for admin role`() = runTest {
        // Given
        val newUser = User(
            UUID.randomUUID(), "User2", UserRole.MATE, listOf(),
            taskIds = listOf()
        )
        coEvery { userRepository.addUser(any(), any()) } returns false

        // When
        val result = createUserUseCase(adminRole, newUser, "password")

        // Then
        assertFalse(result)
        coVerify(exactly = 1) { userRepository.addUser(any(), any()) }
    }
}