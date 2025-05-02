package data.repositories

import logic.models.User
import logic.models.UserRole
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.util.*

class UserRepositoryImplTest {

    private lateinit var repository: UserRepositoryImpl
    private lateinit var user: User

    @BeforeEach
    fun setUp() {
        repository = UserRepositoryImpl()
        user = User(
            id = UUID.randomUUID(),
            name = "Test User",
            hashedPassword = "hashed",
            role = UserRole.MATE,
            projectIds = listOf()
        )
    }

    @Test
    fun `add user successfully`() {
        val result = repository.add(user)
        assertTrue(result)
        assertEquals(user, repository.getById(user.id))
    }

    @Test
    fun `add user with duplicate ID throws exception`() {
        repository.add(user)
        val duplicateUser = user.copy(name = "Duplicate")
        val exception = assertThrows<IllegalArgumentException> {
            repository.add(duplicateUser)
        }
        assertEquals("User with id ${user.id} already exists", exception.message)
    }

    @Test
    fun `delete existing user successfully`() {
        repository.add(user)
        val result = repository.delete(user.id)
        assertTrue(result)
    }

    @Test
    fun `delete non-existing user throws exception`() {
        val fakeId = UUID.randomUUID()
        val exception = assertThrows<NoSuchElementException> {
            repository.delete(fakeId)
        }
        assertEquals("Cannot delete: User with id $fakeId not found", exception.message)
    }

    @Test
    fun `assign user to project successfully`() {
        repository.add(user)
        val projectId = UUID.randomUUID()
        val result = repository.assignToProject(projectId, user.id)
        assertTrue(result)
        assertTrue(repository.getById(user.id).projectIds.contains(projectId))
    }

    @Test
    fun `assign user to already assigned project throws exception`() {
        repository.add(user)
        val projectId = UUID.randomUUID()
        repository.assignToProject(projectId, user.id)
        val exception = assertThrows<IllegalStateException> {
            repository.assignToProject(projectId, user.id)
        }
        assertEquals("Project $projectId is already assigned to user ${user.id}", exception.message)
    }

    @Test
    fun `assign project to non-existing user throws exception`() {
        val fakeId = UUID.randomUUID()
        val projectId = UUID.randomUUID()
        val exception = assertThrows<NoSuchElementException> {
            repository.assignToProject(projectId, fakeId)
        }
        assertEquals("User with id $fakeId not found", exception.message)
    }

    @Test
    fun `revoke user from project successfully`() {
        val projectId = UUID.randomUUID()
        repository.add(user)
        repository.assignToProject(projectId, user.id)
        val result = repository.revokeFromProject(projectId, user.id)
        assertTrue(result)
        assertFalse(repository.getById(user.id).projectIds.contains(projectId))
    }

    @Test
    fun `revoke project not assigned throws exception`() {
        val projectId = UUID.randomUUID()
        repository.add(user)
        val exception = assertThrows<IllegalStateException> {
            repository.revokeFromProject(projectId, user.id)
        }
        assertEquals("Project $projectId is not assigned to user ${user.id}", exception.message)
    }

    @Test
    fun `revoke from non-existing user throws exception`() {
        val fakeId = UUID.randomUUID()
        val projectId = UUID.randomUUID()
        val exception = assertThrows<NoSuchElementException> {
            repository.revokeFromProject(projectId, fakeId)
        }
        assertEquals("User with id $fakeId not found", exception.message)
    }

    @Test
    fun `getById returns user when found`() {
        repository.add(user)
        val result = repository.getById(user.id)
        assertEquals(user.id, result.id)
    }

    @Test
    fun `getById returns correct user from multiple users`() {
        val user2 = user.copy(id = UUID.randomUUID(), name = "User2")
        val user3 = user.copy(id = UUID.randomUUID(), name = "User3")
        repository.add(user)
        repository.add(user2)
        repository.add(user3)

        val result = repository.getById(user2.id)
        assertEquals(user2.id, result.id)
        assertEquals("User2", result.name)
    }

    @Test
    fun `getById throws exception when user not found`() {
        val fakeId = UUID.randomUUID()
        val exception = assertThrows<NoSuchElementException> {
            repository.getById(fakeId)
        }
        assertEquals("User with id $fakeId not found", exception.message)
    }



    @Test
    fun `getAll returns all users`() {
        val user2 = user.copy(id = UUID.randomUUID(), name = "Second")
        repository.add(user)
        repository.add(user2)
        val allUsers = repository.getAll()
        assertEquals(2, allUsers.size)
        assertTrue(allUsers.contains(user))
        assertTrue(allUsers.contains(user2))
    }
}
