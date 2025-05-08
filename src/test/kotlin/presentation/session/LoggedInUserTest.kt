package presentation.session

import logic.models.UserRole
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class LoggedInUserTest {

    private lateinit var loggedInUser: LoggedInUser

    @BeforeEach
    fun setUp() {
        loggedInUser = LoggedInUser(
            id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            name = "testUser",
            role = UserRole.MATE,
            projectIds = emptyList()
        )    }

    @Test
    fun `should return correct user id`() {
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), loggedInUser.id)
    }

    @Test
    fun `should return correct username`() {
        assertEquals("testUser", loggedInUser.name)
    }

    @Test
    fun `should detect if user has specific role`() {
        val userWithRoles = LoggedInUser(
            id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            name = "testUser",
            role = UserRole.ADMIN,
            projectIds = emptyList()
        )

        assertTrue(userWithRoles.role == UserRole.ADMIN)
        assertTrue(UserRole.ADMIN.toString() == "ADMIN")
    }
    @Test
    fun `should detect equality based on user id`() {
        val sameUser = LoggedInUser(
            id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            name = "differentName",
            role = UserRole.MATE,
            projectIds = emptyList()
        )
        val differentUser = LoggedInUser(
            id = UUID.randomUUID(),
            name = "testUser",
            role = UserRole.MATE,
            projectIds = emptyList()
        )

        assertNotEquals(loggedInUser, sameUser)
        assertNotEquals(loggedInUser, differentUser)
    }
}