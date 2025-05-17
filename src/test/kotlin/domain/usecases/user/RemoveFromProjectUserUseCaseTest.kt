package domain.usecases.user

    import domain.models.User
    import domain.models.User.UserRole
    import domain.repositories.UserRepository
    import io.mockk.coEvery
    import io.mockk.coVerify
    import io.mockk.mockk
    import kotlinx.coroutines.test.runTest
    import org.junit.jupiter.api.Assertions.assertEquals
    import org.junit.jupiter.api.BeforeEach
    import org.junit.jupiter.api.Test
    import org.junit.jupiter.api.assertThrows
    import java.util.*

    class RemoveFromProjectUserUseCaseTest {

        private lateinit var userRepository: UserRepository
        private lateinit var getUserByIdUseCase: GetUserByIdUseCase

        private val user = User(
            UUID.randomUUID(), "User1", UserRole.MATE, listOf(),
            taskIds = listOf()
        )

        @BeforeEach
        fun setup() {
            userRepository = mockk()
            getUserByIdUseCase = GetUserByIdUseCase(userRepository)
        }

        @Test
        fun `should return user by id when user exists`() = runTest {
            // Given
            coEvery { userRepository.getUserById(user.id) } returns user

            // When
            val result = getUserByIdUseCase(user.id)

            // Then
            assertEquals(user.id, result.id)
            coVerify(exactly = 1) { userRepository.getUserById(user.id) }
        }

        @Test
        fun `should throw NoSuchElementException when user not found`() = runTest {
            // Given
            val nonExistentUserId = UUID.randomUUID()
            coEvery { userRepository.getUserById(nonExistentUserId) } throws NoSuchElementException("User with id $nonExistentUserId not found")

            // When & Then
            assertThrows<NoSuchElementException> {
                getUserByIdUseCase(nonExistentUserId)
            }

            coVerify(exactly = 1) { userRepository.getUserById(nonExistentUserId) }
        }
    }