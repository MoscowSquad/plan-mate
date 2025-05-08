package presentation.session

import logic.models.UserRole
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class SessionManagerTest {

    private val testUser = LoggedInUser(
        id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
        name = "testUser",
        role = UserRole.MATE,
        projectIds = emptyList()
    )

    @BeforeEach
    fun setUp() {
        SessionManager.currentUser = null
    }

    @AfterEach
    fun tearDown() {
        SessionManager.currentUser = null
    }

    @Test
    fun `currentUser should be null by default`() {
        assertNull(SessionManager.currentUser)
    }

    @Test
    fun `should be able to set and get currentUser`() {
        SessionManager.currentUser = testUser

        assertEquals(testUser, SessionManager.currentUser)
    }

    @Test
    fun `should be able to update currentUser`() {
        SessionManager.currentUser = testUser

        val newUser = LoggedInUser(
            id = UUID.fromString("bcd23456-f89a-12d3-b456-426614174000"),
            name = "anotherUser",
            role = UserRole.ADMIN,
            projectIds = listOf(UUID.randomUUID())
        )

        SessionManager.currentUser = newUser

        assertEquals(newUser, SessionManager.currentUser)
        assertNotEquals(testUser, SessionManager.currentUser)
    }

    @Test
    fun `should be able to clear currentUser`() {
        SessionManager.currentUser = testUser

        SessionManager.currentUser = null

        assertNull(SessionManager.currentUser)
    }
}