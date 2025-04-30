import com.google.common.truth.Truth.assertThat
import data.repositories.AuthenticationRepositoryImpl
import java.util.*
import logic.models.Role
import logic.models.User
import logic.usecases.AuthenticationUseCase
import org.junit.Assert.*
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.jupiter.api.*
import utilities.isValidPasswordFormat
import utilities.toMD5Hash

class UserAuthenticationTest {
    private lateinit var useCase: AuthenticationUseCase
    private lateinit var testRepository: AuthenticationRepositoryImpl

    private val testUser = User(
        id = UUID.randomUUID(),
        username = "testUser",
        hashedPassword = "Test123!".toMD5Hash(),
        role = Role.MATE
    )

    private val testAdmin = User(
        id = UUID.randomUUID(),
        username = "admin",
        hashedPassword = "Admin123!".toMD5Hash(),
        role = Role.ADMIN
    )

    @Test
    fun `validateUsername should throw for blank username`() {
        assertThrows<IllegalArgumentException> {
            useCase.validateUsername("")
        }
    }

    @Test
    fun `validateUsername should return true when username is available`() {
        assertTrue(useCase.validateUsername("newUser"))
    }

    @Test
    fun `validateUsername should return false when username exists (case insensitive)`() {
        testRepository.addUser(User(UUID.randomUUID(), "ExistingUser", "hash", Role.MATE))
        assertFalse(useCase.validateUsername("existinguser"))
    }

    @Test
    fun `validateLogin should return null when user not found`() {
        assertNull(useCase.validateLogin("nonexistent", "password"))
    }

    @Test
    fun `validateLogin should return null when password doesn't match`() {
        testRepository.addUser(User(UUID.randomUUID(), "testUser", "correctHash", Role.MATE))
        assertNull(useCase.validateLogin("testUser", "wrongPassword"))
    }

    @Test
    fun `validateLogin should return role when credentials match`() {
        val password = "correctPassword"
        testRepository.addUser(
            User(
                UUID.randomUUID(),
                "testUser",
                password.toMD5Hash(),
                Role.ADMIN
            )
        )
        assertEquals(Role.ADMIN, useCase.validateLogin("testUser", password))
    }

    @Test
    fun `validateLogin should match username case insensitively`() {
        val password = "password"
        testRepository.addUser(
            User(
                UUID.randomUUID(),
                "TestUser",
                password.toMD5Hash(),
                Role.MATE
            )
        )
        assertNotNull(useCase.validateLogin("testuser", password))
    }

    @Nested
    inner class UsernameValidationTests {
        @BeforeEach
        fun setup() {
            testRepository.addUser(testUser)
        }

        @Test
        fun `should throw IllegalArgumentException when username is empty`() {
            assertThrows<IllegalArgumentException> { useCase.validateUsername("") }
        }

        @Test
        fun `should return false for invalid usernames`() {
            val invalidUsernames = listOf(
                "ab",               // too short
                "a".repeat(21),     // too long
                "user name",        // contains spaces
                "testUser",         // existing username
                "user@name",        // contains special characters
                "user-name"         // contains hyphens
            )

            invalidUsernames.forEach { username ->
                assertFalse(useCase.validateUsername(username))
            }
        }

        @Test
        fun `should return true for valid usernames`() {
            val validUsernames = listOf(
                "user123",
                "User_Name",
                "validUser",
                "name123"
            )

            validUsernames.forEach { username ->
                assertTrue(useCase.validateUsername(username))
            }
        }
    }

    @Nested
    inner class PasswordValidationTests {
        @Test
        fun `should validate password format`() {
            mapOf(
                "short" to false,
                "nouppercase123!" to false,
                "ValidPass123!" to true,
                "MissingSpecial123" to false
            ).forEach { (password, expected) ->
                assertEquals(expected, password.isValidPasswordFormat())
            }
        }
    }

    @Nested
    inner class UserStorageTests {
        @Test
        fun `should store user and return non-null user object`() {
            val user = createTestUser()
            testRepository.addUser(user)
            assertNotNull(testRepository.findByUsername("testUser"))
        }

        @Test
        fun `should store correct username`() {
            val user = createTestUser()
            testRepository.addUser(user)
            assertEquals("testUser", testRepository.findByUsername("testUser")?.username)
        }

        @Test
        fun `should store hashed password, not plain text`() {
            val user = createTestUser()
            testRepository.addUser(user)
            assertNotEquals("Test123!", testRepository.findByUsername("testUser")?.hashedPassword)
        }

        @Test
        fun `should store correct user role`() {
            val user = createTestUser()
            testRepository.addUser(user)
            assertEquals(Role.MATE, testRepository.findByUsername("testUser")?.role)
        }

        @Test
        fun `should prevent duplicate username registration`() {
            val user1 = createTestUser()
            testRepository.addUser(user1)

            val user2 = user1.copy(id = UUID.randomUUID())
            assertThrows<IllegalStateException> {
                testRepository.addUser(user2)
            }
        }

        @Test
        fun `validateLogin should handle duplicate usernames when forced`() {
            // Force add duplicate users (bypassing normal validation)
            val user1 = User(
                UUID.randomUUID(),
                "duplicateUser",
                "pass1".toMD5Hash(),
                Role.MATE
            )
            val user2 = User(
                UUID.randomUUID(),
                "duplicateUser",
                "pass2".toMD5Hash(),
                Role.ADMIN
            )

            testRepository.users.addAll(listOf(user1, user2))

            // Should find first match (implementation dependent)
            assertNotNull(useCase.validateLogin("duplicateUser", "pass1"))
        }

        private fun createTestUser() = User(
            id = UUID.randomUUID(),
            username = "testUser",
            hashedPassword = "Test123!".toMD5Hash(),
            role = Role.MATE
        )
    }
    @Nested
    inner class FindByUsernameTests {
        @Test
        fun `should find user when username exists and case does not match exactly`() {
            // Given
            testRepository.addUser(
                User(
                    UUID.randomUUID(),
                    "ExactCaseUser",
                    "pass".toMD5Hash(),
                    Role.MATE
                )
            )

            // When
            val result = testRepository.findByUsername("exactcaseuser")

            // Then
            assertNotNull(result)
        }

        @Test
        fun `should return user with correct original casing when matched case-insensitively`() {
            // Given
            val username = "ExactCaseUser"
            testRepository.addUser(
                User(
                    UUID.randomUUID(),
                    username,
                    "pass".toMD5Hash(),
                    Role.MATE
                )
            )

            // When
            val result = testRepository.findByUsername("exactcaseuser")

            // Then
            assertEquals(username, result?.username)
        }

        @Test
        fun `should return null after searching full list with no username match`() {
            // Given
            testRepository.addUser(User(UUID.randomUUID(), "User1", "pass1".toMD5Hash(), Role.MATE))
            testRepository.addUser(User(UUID.randomUUID(), "User2", "pass2".toMD5Hash(), Role.ADMIN))
            testRepository.addUser(User(UUID.randomUUID(), "User3", "pass3".toMD5Hash(), Role.MATE))

            // When
            val result = testRepository.findByUsername("NonExistentUser")

            // Then
            assertNull(result)
        }


        @Test
        fun `should return null when no users exist`() {
            val result = testRepository.findByUsername("anyUser")
            assertNull(result)
        }

        @Test
        fun `should find user by exact username match`() {
            val testUser = User(
                id = UUID.randomUUID(),
                username = "ExactMatchUser",
                hashedPassword = "pass".toMD5Hash(),
                role = Role.MATE
            )
            testRepository.addUser(testUser)

            val result = testRepository.findByUsername("ExactMatchUser")
            assertEquals("ExactMatchUser", result?.username)
        }

        @Test
        fun `should find user by lowercase case-insensitive match`() {
            val testUser = User(
                id = UUID.randomUUID(),
                username = "CaseSensitiveUser",
                hashedPassword = "pass".toMD5Hash(),
                role = Role.MATE
            )
            testRepository.addUser(testUser)

            val result = testRepository.findByUsername("casesensitiveuser")
            assertNotNull(result)
        }

        @Test
        fun `should find user by uppercase case-insensitive match`() {
            // Given - add the user to the repository
            testRepository.addUser(
                User(
                    UUID.randomUUID(),
                    "CaseSensitiveUser",  // Stored with mixed case
                    "pass".toMD5Hash(),
                    Role.MATE
                )
            )

            // When - search with all-uppercase version
            val result = testRepository.findByUsername("CASESENSITIVEUSER")

            // Then
            assertNotNull(result)
        }


        @Test
        fun `should find user by original case match`() {
            // Given
            val testUser = User(
                UUID.randomUUID(),
                "CaseSensitiveUser",
                "pass".toMD5Hash(),
                Role.MATE
            )
            testRepository.addUser(testUser)

            // When
            val result = testRepository.findByUsername("CaseSensitiveUser")

            // Then
            Assertions.assertNotNull(result)
        }


        @Test
        fun `should find user by mixed case-insensitive match`() {
            // Given
            val testUser = User(
                UUID.randomUUID(),
                "CaseSensitiveUser",  // Original case
                "pass".toMD5Hash(),
                Role.MATE
            )
            testRepository.addUser(testUser)

            // When
            val result = testRepository.findByUsername("cAsEsEnSiTiVeUsEr") // Mixed case

            // Then
            assertNotNull(result)
        }


        @Test
        fun `should return null for non-existent username`() {
            val result = testRepository.findByUsername("NonExistentUser")
            assertNull(result)
        }

        @Test
        fun `should return first match when duplicate usernames exist`() {
            val username = "DuplicateUser"
            val user1 = User(UUID.randomUUID(), username, "pass1".toMD5Hash(), Role.MATE)
            val user2 = User(UUID.randomUUID(), username, "pass2".toMD5Hash(), Role.ADMIN)
            testRepository.users.addAll(listOf(user1, user2))

            val result = testRepository.findByUsername(username)
            assertEquals("pass1".toMD5Hash(), result?.hashedPassword)
        }

        @Test
        fun `should return null for empty username`() {
            val result = testRepository.findByUsername("")
            assertNull(result)
        }

        @Test
        fun `should return null for username with trailing space`() {
            val result = testRepository.findByUsername("UserWithSpace ")
            assertNull(result)
        }

        @Test
        fun `should return null for username with leading space`() {
            val result = testRepository.findByUsername(" UserWithSpace")
            assertNull(result)
        }

        @Test
        fun `should return null for username with space in middle`() {
            val result = testRepository.findByUsername("User With Space")
            assertNull(result)
        }
    }

}