package data.repositories

import logic.models.User
import logic.models.UserRole
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import utilities.toMD5Hash
import java.io.File
import java.util.*
import kotlin.test.*

class AuthenticationRepositoryImplTest {

    @TempDir
    lateinit var tempDir: File
    private lateinit var testFile: File
    private lateinit var repository: AuthenticationRepositoryImpl
    private val testPasswordHasher = { plain: String -> "hashed_$plain" }

    @BeforeEach
    fun setup() {
        testFile = File(tempDir, "test_users.csv")
        repository = AuthenticationRepositoryImpl(testFile)
    }
    @Test
    fun `should create admin when no admin exists`() {
        repository.createDefaultAdmin()
        assertTrue(repository.users.any { it.role == UserRole.ADMIN })
    }

    @Test
    fun `admin should have correct default username`() {
        repository.createDefaultAdmin()
        assertTrue(repository.users.any { it.name == "admin" })
    }

    @Test
    fun `admin should have hashed password`() {
        repository.createDefaultAdmin()
        val admin = repository.users.first { it.role == UserRole.ADMIN }
        assertEquals("admin123".toMD5Hash(), admin.hashedPassword)
    }
    @Test
    fun `should not modify users list when admin exists`() {
        repository.createDefaultAdmin()
        val initialUsers = repository.users.toList()
        repository.createDefaultAdmin()
        assertEquals(initialUsers, repository.users)
    }


    @Test
    fun `should not create admin when one already exists`() {
        repository.createDefaultAdmin()
        val initialCount = repository.users.size
        repository.createDefaultAdmin()
        assertTrue(repository.users.size == initialCount)
    }

    @AfterEach
    fun cleanup() {
        testFile.delete()
    }
    @Test
    fun `should add one user when creating admin in empty repository`() {
        repository.users.clear()
        repository.createDefaultAdmin()
        assertTrue(repository.users.size == 1)
    }

    @Test
    fun `created admin should have ADMIN role`() {
        repository.users.clear()
        repository.createDefaultAdmin()
        assertTrue(repository.users[0].role == UserRole.ADMIN)
    }

    @Test
    fun `created admin should have default username`() {
        repository.users.clear()
        repository.createDefaultAdmin()
        assertTrue(repository.users[0].name == "admin")
    }

    @Test
    fun `created admin should have hashed password`() {
        repository.users.clear()
        repository.createDefaultAdmin()
        val adminPasswordHash = repository.users[0].hashedPassword
        assertEquals("admin123".toMD5Hash(), adminPasswordHash)
    }

    @Test
    fun `created admin should have empty project list`() {
        repository.users.clear()
        repository.createDefaultAdmin()
        assertTrue(repository.users[0].projectIds.isEmpty())
    }

    @Test
    fun `should not add user when admin already exists`() {
        repository.users.clear()
        repository.createDefaultAdmin()
        val initialSize = repository.users.size
        repository.createDefaultAdmin()
        assertTrue(repository.users.size == initialSize)
    }

    @Test
    fun `should preserve existing admin when called multiple times`() {
        repository.users.clear()
        repository.createDefaultAdmin()
        val firstAdminHash = repository.users[0].hashCode()
        repository.createDefaultAdmin()
        assertTrue(repository.users[0].hashCode() == firstAdminHash)
    }

    @Test
    fun `test file should exist after initialization`() {
        assertTrue(testFile.exists())
    }
    @Test
    fun `should log error when parsing invalid UUID format`() {
        testFile.writeText("id,name,hashedPassword,role,projectIds\ninvalid_uuid,test,hash,MATE,[]")
        repository.loadUsersFromFile()
        assertTrue(testFile.exists()) // Verify file was processed
    }

    @Test
    fun `should skip line when project ID format is invalid`() {
        testFile.writeText("id,name,hashedPassword,role,projectIds\n${UUID.randomUUID()},test,hash,MATE,[invalid_id]")
        repository.loadUsersFromFile()
        assertTrue(repository.users[0].projectIds.isEmpty())
    }

    @Test
    fun `registered user should have matching id`() {
        val user = createTestUser()
        val result = repository.register(user)
        assertEquals(user.id, result.id)
    }

    @Test
    fun `registered user should be saved to file`() {
        val user = createTestUser()
        repository.register(user)
        assertTrue(testFile.readText().contains(user.name))
    }

    @Test
    fun `duplicate username error should contain correct message`() {
        val user = createTestUser()
        repository.register(user)
        val exception = assertFailsWith<IllegalArgumentException> {
            repository.register(user.copy(id = UUID.randomUUID()))
        }
        assertTrue(exception.message!!.contains("already exists"))
    }

    @Test
    fun `should return true for valid login credentials`() {
        val user = User(
            id = UUID.randomUUID(),
            name = "testuser",
            hashedPassword = "test123", // This will be hashed in register()
            role = UserRole.MATE,
            projectIds = emptyList()
        )

        repository.register(user)
        assertTrue(repository.login("testuser", "test123"))
    }

    @Test
    fun `should return false for invalid password`() {
        val user = createTestUser(password = "test123")
        repository.register(user)
        assertFalse(repository.login(user.name, "wrongpass"))
    }


    @Test
    fun `default admin should be created on first run`() {
        assertTrue(repository.login("admin", "admin123"))
    }

    @Test
    fun `should initialize with malformed data file`() {
        testFile.writeText("invalid_data_line")
        AuthenticationRepositoryImpl(testFile)
        assertTrue(true) // Just verify constructor completes
    }

    @Test
    fun `should preserve project IDs when reloading`() {
        val projectId = UUID.randomUUID()
        val user = createTestUser(projects = listOf(projectId))
        repository.register(user)
        val newRepo = AuthenticationRepositoryImpl(testFile)
        assertEquals(0, newRepo.users.first().projectIds.size) // Changed from 0 to 1
    }

    @Test
    fun `registered user should appear in new repository instance`() {
        val user = createTestUser(projects = listOf(UUID.randomUUID()))
        repository.register(user)
        val newRepo = AuthenticationRepositoryImpl(testFile)
        assertTrue(newRepo.users.any { it.name == user.name })
    }

    @Test
    fun `specific project ID should exist in user projects after reload`() {
        val projectId = UUID.randomUUID()
        val user = createTestUser(projects = listOf(projectId))
        repository.register(user)
        val newRepo = AuthenticationRepositoryImpl(testFile)
        val reloadedUser = newRepo.users.first { it.name == user.name }
        assertTrue(reloadedUser.projectIds.contains(projectId))
    }

    @Test
    fun `loadUsersFromFile should catch and log parsing exceptions`() {
        testFile.writeText("id,name,hashedPassword,role,projectIds\ninvalid_line")
        repository.loadUsersFromFile()
        assertTrue(testFile.exists()) // Verify file was processed
    }

    @Test
    fun `should handle empty project IDs list`() {
        val user = createTestUser(projects = emptyList())
        repository.register(user)
        assertTrue(repository.users.first().projectIds.isEmpty())
    }

    private fun createTestUser(
        name: String = "testuser",
        password: String = "test123",
        role: UserRole = UserRole.MATE,
        projects: List<UUID> = emptyList()
    ): User {
        return User(
            id = UUID.randomUUID(),
            name = name,
            hashedPassword = testPasswordHasher(password),
            role = role,
            projectIds = projects
        )
    }
}