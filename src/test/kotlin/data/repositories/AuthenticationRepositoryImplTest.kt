package data.repositories

import com.google.common.truth.Truth
import data.csv_parser.CsvHandler
import data.csv_parser.UserCsvParser
import data.datasource.UserDataSource
import logic.models.User
import logic.models.UserRole
import logic.util.toMD5Hash
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AuthenticationRepositoryImplTest {
    @TempDir
    lateinit var tempDir: File
    private lateinit var testFile: File
    private lateinit var csvHandler: CsvHandler
    private lateinit var csvParser: UserCsvParser
    private lateinit var dataSource: UserDataSource
    private lateinit var repository: AuthenticationRepositoryImpl


    @BeforeEach
    fun setup() {
        testFile = File(tempDir, "test_users.csv").apply {
            if (exists().not())
                createNewFile()
        }
        csvHandler = CsvHandler(testFile)
        csvParser = UserCsvParser()
        dataSource = UserDataSource(csvHandler, csvParser)
        repository = AuthenticationRepositoryImpl(dataSource)
    }

    @Test
    fun `created admin has correct username`() {
        repository.createDefaultAdmin()
        assertEquals("admin", repository.users.first { it.role == UserRole.ADMIN }.name)
    }

    @Test
    fun `login returns true when username and password match`() {
        // Arrange
        val testUser = createTestUser(name = "validUser", password = "correctPass")
        repository.users.add(testUser)

        // Act & Assert
        assertTrue(repository.login("validUser", "correctPass"))
    }

    @Test
    fun `login returns false when username matches but password hash differs`() {
        // Arrange
        val testUser = createTestUser(
            name = "user1",
            password = "pass1" // Will be hashed to hash1
        )
        repository.users.add(testUser)

        // Act & Assert
        assertFalse(
            repository.login("user1", "pass2"),
            "Should return false when password hashes don't match"
        )
    }

    @Test
    fun `login is case sensitive for usernames`() {
        // Arrange
        val testUser = createTestUser(
            name = "User1", // With capital U
            password = "pass1"
        )
        repository.users.add(testUser)

        // Act & Assert
        assertFalse(
            repository.login("user1", "pass1"),
            "Should be case sensitive for usernames"
        )
    }

    @Test
    fun `login returns false when username exists but password is wrong`() {
        // Arrange
        val testUser = createTestUser(name = "validUser", password = "correctPass")
        repository.users.add(testUser)

        // Act & Assert
        assertFalse(repository.login("validUser", "wrongPass"))
    }

    @Test
    fun `login returns false when username does not exist`() {
        assertFalse(repository.login("nonexistentUser", "anyPass"))
    }

    @Test
    fun `should create admin when no admin exists`() {
        repository.createDefaultAdmin()
        assertTrue(repository.users.any { it.role == UserRole.ADMIN })
    }

    @Test
    fun `login returns true for matching credentials`() {
        val testPassword = "correct123"
        val testUser = User(
            id = UUID.randomUUID(),
            name = "test user",
            hashedPassword = testPassword.toMD5Hash(),
            role = UserRole.MATE,
            projectIds = emptyList()
        )
        repository.users.add(testUser)
        assertTrue(repository.login("test user", testPassword))
    }

    @Test
    fun `loadUsersFromFile skips header line`() {
        // Arrange
        testFile.writeText("id,name,password,role,projects\n${UUID.randomUUID()},user1,hash1,MATE,[]")
        assertEquals(1, repository.users.size)
    }

    @Test
    fun `loadUsersFromFile handles empty file`() {
        testFile.writeText("id,name,password,role,projects") // Only header
        assertTrue(repository.users.isEmpty())
    }

    @Test
    fun `login returns false for wrong password`() {
        val testUser = User(
            id = UUID.randomUUID(),
            name = "test user",
            hashedPassword = "correct123".toMD5Hash(),
            role = UserRole.MATE,
            projectIds = emptyList()
        )
        repository.users.add(testUser)
        assertFalse(repository.login("test user", "wrong123"))
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
        Truth.assertThat(repository.users.size).isEqualTo(initialCount)
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
        Truth.assertThat(repository.users[0].role).isEqualTo(UserRole.ADMIN)
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
        Truth.assertThat(repository.users.size).isEqualTo(initialSize)
    }

    @Test
    fun `should preserve existing admin when called multiple times`() {
        repository.users.clear()
        repository.createDefaultAdmin()
        val firstAdminHash = repository.users[0].hashCode()
        repository.createDefaultAdmin()
        Truth.assertThat(repository.users[0].hashCode()).isEqualTo(firstAdminHash)
    }

    @Test
    fun `test file should exist after initialization`() {
        assertTrue(testFile.exists())
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
    fun `should return false for invalid password`() {
        val user = createTestUser(password = "test123")
        repository.register(user)
        assertFalse(repository.login(user.name, "wrong pass"))
    }

    @Test
    fun `should handle empty project IDs list`() {
        val user = createTestUser(projects = emptyList())
        repository.register(user)
        assertTrue(repository.users.first().projectIds.isEmpty())
    }

    private fun createTestUser(
        name: String = "test user",
        password: String = "test123",
        role: UserRole = UserRole.MATE,
        projects: List<UUID> = emptyList()
    ): User {
        return User(
            id = UUID.randomUUID(),
            name = name,
            hashedPassword = password.toMD5Hash(),
            role = role,
            projectIds = projects
        )
    }
}