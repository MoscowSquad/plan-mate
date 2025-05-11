package data.repositories

import data.csv_data.datasource.UserDataSource
import data.csv_data.mappers.toDto
import data.csv_data.repositories.AuthenticationRepositoryImpl
import di.SessionManager
import io.mockk.*
import logic.models.User
import logic.models.UserRole
import logic.util.UserNotFoundException
import logic.util.toMD5Hash
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class AuthenticationRepositoryImplTest {
    private lateinit var userDataSource: UserDataSource
    private lateinit var repository: AuthenticationRepositoryImpl
    private lateinit var testUser: User

    @BeforeEach
    fun setUp() {
        userDataSource = mockk(relaxed = true)
        repository = AuthenticationRepositoryImpl(userDataSource)

        testUser = User(
            id = UUID.randomUUID(),
            name = "testUser",
            hashedPassword = "password123".toMD5Hash(),
            role = UserRole.MATE,
            projectIds = emptyList()
        )

        mockkObject(SessionManager)
        every { SessionManager.currentUser = any() } just Runs
    }

    @Test
    fun `init should fetch users from data source`() {
        // Given
        val userList = listOf(testUser)
        val userDtoList = userList.map { it.toDto() }

        // When
        every { userDataSource.fetch() } returns userDtoList
        repository = AuthenticationRepositoryImpl(userDataSource)

        // Then
        assertEquals(1, repository.users.size)
        assertEquals(testUser.id, repository.users[0].id)
        verify { userDataSource.fetch() }
    }

    @Test
    fun `register should add user and update data source`() {
        // Given
        repository.users.clear()
        every { userDataSource.save(any()) } just Runs

        // When
        val result = repository.register(testUser)

        // Then
        assertEquals(testUser, result)
        assertEquals(1, repository.users.size)
        verify { userDataSource.save(any()) }
        verify { SessionManager.currentUser = any() }
    }

    @Test
    fun `register should throw exception for duplicate username`() {
        // Given
        repository.users.add(testUser)

        // When and Then
        val duplicateUser = testUser.copy(id = UUID.randomUUID())
        assertFailsWith<IllegalArgumentException> {
            repository.register(duplicateUser)
        }

        verify(exactly = 0) { userDataSource.save(any()) }
    }

    @Test
    fun `login should update session and return true for valid credentials`() {
        // Given
        repository.users.clear()
        repository.users.add(testUser)

        // When
        val result = repository.login("testUser", "password123")

        // Then
        assertTrue(result)
        verify { SessionManager.currentUser = any() }
    }

    @Test
    fun `login should throw UserNotFoundException for invalid credentials`() {
        // Given
        repository.users.clear()
        repository.users.add(testUser)

        // When/Then
        assertFailsWith<UserNotFoundException> {
            repository.login("testUser", "wrongPassword")
        }

        verify(exactly = 0) { SessionManager.currentUser = any() }
    }

    @Test
    fun `create Default Admin should register admin user when no admin exists`() {
        // Given
        repository.users.clear()
        every { userDataSource.save(any()) } just Runs

        // When
        repository.createDefaultAdmin()

        // Then
        assertEquals(1, repository.users.size)
        assertEquals(UserRole.ADMIN, repository.users[0].role)
        assertEquals("admin", repository.users[0].name)
        verify { userDataSource.save(any()) }
    }

    @Test
    fun `create Default Admin should not create admin when one already exists`() {
        // Given
        repository.users.clear()
        val adminUser = User(
            id = UUID.randomUUID(),
            name = "existingAdmin",
            hashedPassword = "adminPass".toMD5Hash(),
            role = UserRole.ADMIN,
            projectIds = emptyList()
        )
        repository.users.add(adminUser)

        // When
        repository.createDefaultAdmin()

        // Then
        assertEquals(1, repository.users.size)
        assertEquals("existingAdmin", repository.users[0].name)
        verify(exactly = 0) { userDataSource.save(any()) }
    }

    @Test
    fun `create Default Admin should not register admin when any user with ADMIN role exists`() {
        // Given
        repository.users.clear()
        val normalUser = User(
            id = UUID.randomUUID(),
            name = "normalUser",
            hashedPassword = "password123".toMD5Hash(),
            role = UserRole.MATE,
            projectIds = emptyList()
        )
        val adminUser = User(
            id = UUID.randomUUID(),
            name = "someAdmin",
            hashedPassword = "adminPass".toMD5Hash(),
            role = UserRole.ADMIN,
            projectIds = emptyList()
        )
        repository.users.add(normalUser)
        repository.users.add(adminUser)

        // When
        repository.createDefaultAdmin()

        // Then
        assertEquals(2, repository.users.size)
        verify(exactly = 0) { userDataSource.save(any()) }
    }

    @Test
    fun `login should throw UserNotFoundException for non-existent user`() {
        // Given
        repository.users.clear()
        repository.users.add(testUser)

        // When/Then
        assertFailsWith<UserNotFoundException> {
            repository.login("nonExistentUser", "anyPassword")
        }

        verify(exactly = 0) { SessionManager.currentUser = any() }
    }

}