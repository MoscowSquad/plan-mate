package domain.usecases.user

import domain.models.User
import domain.models.User.UserRole
import domain.repositories.UserRepository
import domain.util.UnauthorizedAccessException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class AssignProjectToUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var assignProjectToUserUseCase: AssignProjectToUserUseCase

    private val adminRole = UserRole.ADMIN
    private val mateRole = UserRole.MATE
    private val user = User(
        UUID.randomUUID(), "User1", UserRole.MATE, listOf(),
        taskIds = listOf()
    )
    private val projectId = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        assignProjectToUserUseCase = AssignProjectToUserUseCase(userRepository)
    }

    @Test
    fun `should throw UnauthorizedAccessException for mates try to assign project`(): Unit = runTest {
        // Given

        // When & Then
        assertThrows<UnauthorizedAccessException> {
             assignProjectToUserUseCase(mateRole, projectId, user.id)
        }
    }


    @Test
    fun `should assign project for users when admins try to assign`() = runTest {
        // Given
        coEvery { userRepository.assignUserToProject(projectId, user.id) } returns true

        // When
        val result = assignProjectToUserUseCase(adminRole, projectId, user.id)

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { userRepository.assignUserToProject(projectId, user.id) }
    }
}