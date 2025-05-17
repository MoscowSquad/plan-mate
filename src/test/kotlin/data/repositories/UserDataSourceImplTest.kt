package data.repositories

import data.csv_data.datasource.UserDataSource
import data.csv_data.dto.UserDto
import data.csv_data.repositories.UserRepositoryImpl
import domain.models.User
import domain.models.User.UserRole
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class UserRepositoryImplTest {

    private lateinit var dataSource: UserDataSource
    private lateinit var repository: UserRepositoryImpl
    private lateinit var user: User
    private val hashedPassword = "hashed"

    @BeforeEach
    fun setUp() {
        dataSource = mockk(relaxed = true)
        repository = UserRepositoryImpl(dataSource)
        user = User(
            id = UUID.randomUUID(),
            name = "Test User",
            role = UserRole.MATE,
            projectIds = listOf(),
            taskIds = emptyList()
        )
    }

    @Test
    fun `addUser successfully adds a user to the repository`() = runTest {
        // When
        val result = repository.addUser(user, hashedPassword)

        // Then
        assertTrue(result)
        assertEquals(user, repository.getUserById(user.id))
    }

    @Test
    fun `addUser throws exception when adding user with duplicate ID`(): Unit = runTest {
        // Given
        repository.addUser(user, hashedPassword)
        val duplicateUser = user.copy(name = "Duplicate")

        // When & Then
        assertThrows<IllegalArgumentException> {
            runTest { repository.addUser(duplicateUser, hashedPassword) }
        }
    }

    @Test
    fun `deleteUser successfully removes existing user`(): Unit = runTest {
        // Given
        repository.addUser(user, hashedPassword)

        // When
        val result = repository.deleteUser(user.id)

        // Then
        assertTrue(result)
        assertThrows<NoSuchElementException> {
            runTest { repository.getUserById(user.id) }
        }
    }

    @Test
    fun `deleteUser throws exception when user does not exist`(): Unit = runTest {
        // Given
        val nonExistingId = UUID.randomUUID()

        // When & Then
        assertThrows<NoSuchElementException> {
            runTest { repository.deleteUser(nonExistingId) }
        }
    }

    @Test
    fun `assignUserToProject successfully assigns project to user`() = runTest {
        // Given
        repository.addUser(user, hashedPassword)
        val projectId = UUID.randomUUID()

        // When
        val result = repository.assignUserToProject(projectId, user.id)

        // Then
        assertTrue(result)
        val updatedUser = repository.getUserById(user.id)
        assertTrue(updatedUser.projectIds.contains(projectId))
    }

    @Test
    fun `assignUserToProject throws exception when user already assigned to project`(): Unit = runTest {
        // Given
        repository.addUser(user, hashedPassword)
        val projectId = UUID.randomUUID()
        repository.assignUserToProject(projectId, user.id)

        // When & Then
        assertThrows<IllegalStateException> {
            runTest { repository.assignUserToProject(projectId, user.id) }
        }
    }

    @Test
    fun `assignUserToProject throws exception when user does not exist`(): Unit = runTest {
        // Given
        val nonExistingId = UUID.randomUUID()
        val projectId = UUID.randomUUID()

        // When & Then
        assertThrows<NoSuchElementException> {
            runTest { repository.assignUserToProject(projectId, nonExistingId) }
        }
    }

    @Test
    fun `unassignUserFromProject successfully removes project from user`() = runTest {
        // Given
        repository.addUser(user, hashedPassword)
        val projectId = UUID.randomUUID()
        repository.assignUserToProject(projectId, user.id)

        // When
        val result = repository.unassignUserFromProject(projectId, user.id)

        // Then
        assertTrue(result)
        val updatedUser = repository.getUserById(user.id)
        assertFalse(updatedUser.projectIds.contains(projectId))
    }

    @Test
    fun `unassignUserFromProject throws exception when project not assigned to user`(): Unit = runTest {
        // Given
        repository.addUser(user, hashedPassword)
        val projectId = UUID.randomUUID()

        // When & Then
        assertThrows<IllegalStateException> {
            runTest { repository.unassignUserFromProject(projectId, user.id) }
        }
    }

    @Test
    fun `unassignUserFromProject throws exception when user does not exist`(): Unit = runTest {
        // Given
        val nonExistingId = UUID.randomUUID()
        val projectId = UUID.randomUUID()

        // When & Then
        assertThrows<NoSuchElementException> {
            runTest { repository.unassignUserFromProject(projectId, nonExistingId) }
        }
    }

    @Test
    fun `getUserById returns correct user when found`() = runTest {
        // Given
        repository.addUser(user, hashedPassword)

        // When
        val result = repository.getUserById(user.id)

        // Then
        assertEquals(user.id, result.id)
        assertEquals(user.name, result.name)
        assertEquals(user.role, result.role)
    }

    @Test
    fun `getUserById returns correct user from multiple users`() = runTest {
        // Given
        val user2 = user.copy(id = UUID.randomUUID(), name = "User2")
        val user3 = user.copy(id = UUID.randomUUID(), name = "User3")
        repository.addUser(user, hashedPassword)
        repository.addUser(user2, hashedPassword)
        repository.addUser(user3, hashedPassword)

        // When
        val result = repository.getUserById(user2.id)

        // Then
        assertEquals(user2.id, result.id)
        assertEquals("User2", result.name)
    }

    @Test
    fun `getUserById throws exception when user not found`(): Unit = runTest {
        // Given
        val nonExistingId = UUID.randomUUID()

        // When & Then
        assertThrows<NoSuchElementException> {
            runTest { repository.getUserById(nonExistingId) }
        }
    }

    @Test
    fun `getAllUsers returns all users in the repository`() = runTest {
        // Given
        val user2 = user.copy(id = UUID.randomUUID(), name = "Second User")
        repository.addUser(user, hashedPassword)
        repository.addUser(user2, hashedPassword)

        // When
        val result = repository.getAllUsers()

        // Then
        assertEquals(2, result.size)
        assertTrue(result.any { it.id == user.id })
        assertTrue(result.any { it.id == user2.id })
    }

    @Test
    fun `getAllUsers loads data from data source when users list is empty`() = runTest {
        // Given
        val userDtos = listOf(
            UserDto(
                UUID.randomUUID().toString(),
                "User1",
                "password1",
                "MATE",
                emptyList(),
                emptyList()
            ),
            UserDto(
                UUID.randomUUID().toString(),
                "User2",
                "password2",
                "MATE",
                emptyList(),
                emptyList()
            )
        )

        coEvery { dataSource.fetch() } returns userDtos

        // When
        val result = repository.getAllUsers()

        // Then
        coVerify { dataSource.fetch() }
        assertEquals(2, result.size)
    }

    @Test
    fun `getAllUsers returns cached data when users list is not empty`() = runTest {
        // Given
        repository.addUser(user, hashedPassword)

        // When
        repository.getAllUsers()

        // Then
        coVerify(exactly = 0) { dataSource.fetch() }
    }

    @Test
    fun `assignUserToTask successfully assigns task to user`() = runTest {
        // Given
        repository.addUser(user, hashedPassword)
        val taskId = UUID.randomUUID()

        // When
        val result = repository.assignUserToTask(taskId, user.id)

        // Then
        assertTrue(result)
        val updatedUser = repository.getUserById(user.id)
        assertTrue(updatedUser.taskIds.contains(taskId))
    }

    @Test
    fun `assignUserToTask throws exception when task already assigned to user`(): Unit = runTest {
        // Given
        repository.addUser(user, hashedPassword)
        val taskId = UUID.randomUUID()
        repository.assignUserToTask(taskId, user.id)

        // When & Then
        assertThrows<IllegalStateException> {
            runTest { repository.assignUserToTask(taskId, user.id) }
        }
    }

    @Test
    fun `assignUserToTask throws exception when user does not exist`(): Unit = runTest {
        // Given
        val nonExistingId = UUID.randomUUID()
        val taskId = UUID.randomUUID()

        // When & Then
        assertThrows<NoSuchElementException> {
            runTest { repository.assignUserToTask(taskId, nonExistingId) }
        }
    }
}