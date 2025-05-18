package data.repositories

import data.csv_data.datasource.UserDataSource
import data.csv_data.mappers.toDto
import data.csv_data.mappers.toUser
import data.csv_data.repositories.AuthenticationRepositoryImpl
import data.session_manager.SessionManager
import domain.models.User
import domain.models.User.UserRole
import domain.util.UserNotFoundException
import domain.util.toMD5Hash
import io.mockk.*
import kotlinx.coroutines.test.runTest
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
    private val hashedPassword = toMD5Hash("password123")

    @BeforeEach
    fun setUp() {
        userDataSource = mockk(relaxed = true)
        testUser = User(
            id = UUID.randomUUID(),
            name = "testUser",
            role = UserRole.MATE,
            projectIds = emptyList(),
            taskIds = emptyList()
        )

        every { userDataSource.fetch() } returns listOf(testUser.toDto(hashedPassword))

        repository = AuthenticationRepositoryImpl(userDataSource)

        mockkObject(SessionManager)
        every { SessionManager.currentUser = any() } just Runs
    }

    @Test
    fun `init should fetch users from data source`() {
        // Then
        verify { userDataSource.fetch() }
        assertEquals(1, repository.users.size)
        assertEquals(testUser.id, repository.users[0].toUser().id)
    }

    @Test
    fun `register should add user and update data source`() = runTest {
        // Given
        val hashedPassword = toMD5Hash("password456")
        val newUser = User(
            id = UUID.randomUUID(),
            name = "newUser",
            role = UserRole.MATE,
            projectIds = emptyList(),
            taskIds = emptyList()
        )
        every { userDataSource.save(any()) } just Runs

        // When
        val result = repository.register(newUser, hashedPassword)

        // Then
        assertEquals(newUser, result)
        assertTrue(repository.users.contains(newUser.toDto(hashedPassword)))
        verify { userDataSource.save(any()) }
        verify { SessionManager.currentUser = any() }
    }

    @Test
    fun `register should throw exception for duplicate username`() = runTest {
        // Given
        val duplicateUser = testUser.copy(id = UUID.randomUUID())

        // When and Then
        assertFailsWith<IllegalArgumentException> {
            repository.register(duplicateUser, hashedPassword)
        }

        verify(exactly = 0) { userDataSource.save(any()) }
    }

    @Test
    fun `login should update session and return user for valid credentials`() = runTest {
        // When
        val result = repository.login("testUser", "password123")

        // Then
        assertEquals(testUser, result)
        verify { SessionManager.currentUser = any() }
    }

    @Test
    fun `login should throw UserNotFoundException for invalid credentials`() = runTest {
        // When & Then
        assertFailsWith<UserNotFoundException> {
            repository.login("testUser", "wrongPassword")
        }

        verify(exactly = 0) { SessionManager.currentUser = any() }
    }

    @Test
    fun `login should throw UserNotFoundException for non-existent user`() = runTest {
        // When & Then
        assertFailsWith<UserNotFoundException> {
            repository.login("nonExistentUser", "anyPassword")
        }

        verify(exactly = 0) { SessionManager.currentUser = any() }
    }
}