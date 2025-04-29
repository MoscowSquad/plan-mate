import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import java.util.*
import logic.models.Role
import logic.models.User
import org.junit.jupiter.api.assertThrows
import utilities.toMD5Hash

class UserAuthenticationTest {

 private val testUserId = UUID.randomUUID()
 private val testAdminId = UUID.randomUUID()

 private val testUser = User(
  id = testUserId,
  username = "testUser",
  hashedPassword = "Test123!".toMD5Hash(),
  role = Role.MATE
 )

 private val testAdmin = User(
  id = testAdminId,
  username = "admin",
  hashedPassword = "Admin123!".toMD5Hash(),
  role = Role.ADMIN
 )

 @Nested
 inner class UsernameValidationTests {
  @Test
  fun `should throw exception when username is empty`() {
   // Given
   val input = ""
   // When & Then
   assertThrows<IllegalArgumentException> {
    validateUsernameWithException(input)
   }
  }

  @Test
  fun `should return false when username is empty`() {
   // Given
   val input = ""
   // When
   val result = validateUsername(input)
   // Then
   assertFalse { result.isValid }
  }

  @Test
  fun `should throw when username is too short`() {
   // Given
   val input = "ab"

   // When & Then
   assertThrows<IllegalArgumentException> {
    validateUsername(input)
   }
  }

  @Test
  fun `should return false when username is too short`() {
   // Given
   val input = "ab"
   // When
   val result = validateUsername(input)
   // Then
   assertFalse { result.isValid }
  }

  @Test
  fun `should throw when username is too long`() {
   // Given
   val input = "a".repeat(21)

   // When & Then
   assertThrows<IllegalArgumentException> {
    validateUsername(input)
   }
  }

  @Test
  fun `should return false when username is too long`() {
   // Given
   val input = "a".repeat(21)
   // When
   val result = validateUsername(input)
   // Then
   assertFalse { result.isValid }
  }

  @Test
  fun `should throw when username contains spaces`() {
   assertThrows<IllegalArgumentException> {
    validateUsername("user name")
   }
   // Only checks that an exception is thrown, not the message
  }

  @Test
  fun `should return false when username contains spaces`() {
   // Given
   val input = "user name"
   // When
   val result = validateUsername(input)
   // Then
   assertFalse { result.isValid }
  }

  @Test
  fun `should throw when username contains special characters`() {
   assertThrows<IllegalArgumentException> {
    validateUsername("user@name")
   }
  }

  @Test
  fun `should return false when username contains special characters`() {
   // Given
   val input = "user@name"
   // When
   val result = validateUsername(input)
   // Then
   assertFalse { result.isValid }
  }

  @Test
  fun `should return true when username is valid`() {
   // Given
   val input = "test_user"
   // When
   val result = validateUsername(input)
   // Then
   assertTrue { result.isValid }
  }

  @Test
  fun `should return valid result when username is valid`() {
   // Given
   val input = "test_user"

   // When
   val result = validateUsername(input)

   // Then
   assertTrue(result.isValid)
  }

  @Nested
  inner class PasswordValidationTests {
   @Test
   fun `should return false when password is shorter than 8 characters`() {
    // Given
    val password = "Pass1!"
    // When
    val result = password.isValidPasswordFormat()
    // Then
    assertFalse { result }
   }

   @Test
   fun `should return false when password has no uppercase letter`() {
    // Given
    val password = "password123!"
    // When
    val result = password.isValidPasswordFormat()
    // Then
    assertFalse { result }
   }

   @Test
   fun `should return false when password has no lowercase letter`() {
    // Given
    val password = "PASSWORD123!"
    // When
    val result = password.isValidPasswordFormat()
    // Then
    assertFalse { result }
   }

   @Test
   fun `should return false when password has no number`() {
    // Given
    val password = "Password!"
    // When
    val result = password.isValidPasswordFormat()
    // Then
    assertFalse { result }
   }

   @Test
   fun `should return false when password has no special character`() {
    // Given
    val password = "Password123"
    // When
    val result = password.isValidPasswordFormat()
    // Then
    assertFalse { result }
   }

   @Test
   fun `should return true when password is valid`() {
    // Given
    val password = "ValidPass123!"
    // When
    val result = password.isValidPasswordFormat()
    // Then
    assertTrue { result }
   }

   @Test
   fun `should return different hash from original password`() {
    // Given
    val password = "Test123!"
    // When
    val hashed = password.toMD5Hash()
    // Then
    assertNotEquals(password, hashed)
   }

   @Test
   fun `should return 32-character hash`() {
    // Given
    val password = "Test123!"
    // When
    val hashed = password.toMD5Hash()
    // Then
    assertEquals(32, hashed.length)
   }
  }

  @Nested
  inner class LoginValidationTests {
   private val userRepository = FakeUserRepository().apply {
    addUser(testUser)
    addUser(testAdmin)
   }

   @Test
   fun `should return false when username is incorrect`() {
    // Given
    val username = "wrongUser"
    val password = "Test123!"
    // When
    val result = validateLogin(username, password, userRepository)
    // Then
    assertFalse { result.isValid }
   }

   @Test
   fun `should throw when username is incorrect`() {
    assertThrows<AuthenticationException> {
     validateLogin("wrongUser", "Test123!", userRepository)
    }
   }

   @Test
   fun `should return false when password is incorrect`() {
    // Given
    val username = "testUser"
    val password = "wrongPass"
    // When
    val result = validateLogin(username, password, userRepository)
    // Then
    assertFalse { result.isValid }
   }

   @Test
   fun `should throw when password is incorrect`() {
    // Given
    val username = "testUser"
    val password = "wrongPass"

    // When & Then
    assertThrows<AuthenticationException> {
     validateLogin(username, password, userRepository)
    }
   }

   @Test
   fun `should return true when credentials are correct`() {
    // Given
    val username = "testUser"
    val password = "Test123!"
    // When
    val result = validateLogin(username, password, userRepository)
    // Then
    assertTrue { result.isValid }
   }

   @Test
   fun `should return null error message when credentials are correct`() {
    // Given
    val username = "testUser"
    val password = "Test123!"
    // When
    val result = validateLogin(username, password, userRepository)
    // Then
    assertNull(result.errorMessage)
   }

   @Test
   fun `should return correct user role after successful login`() {
    // Given
    val username = "testUser"
    val password = "Test123!"
    // When
    val result = validateLogin(username, password, userRepository)
    // Then
    assertEquals(Role.MATE, result.user?.role)
   }
  }

  @Nested
  inner class UserStorageTests {
   private val userRepository = UserRepository()

   @Test
   fun `should throw when password is incorrect`() {
    // Given
    val username = "testUser"
    val password = "wrongPass"

    // When & Then
    assertThrows<AuthenticationException> {
     validateLogin(username, password, userRepository)
    }
   }

   @Test
   fun `should store username correctly`() {
    // Given
    val user = testUser

    // When
    userRepository.addUser(user)
    val retrievedUser = userRepository.findByUsername("testUser")

    // Then
    assertTrue(
     retrievedUser?.username == user.username,
     "Stored username '${retrievedUser?.username}' should match original '${user.username}'"
    )
   }


   @Test
   fun `should store hashed password correctly`() {
    // Given
    val user = testUser

    // When
    userRepository.addUser(user)
    val retrievedUser = userRepository.findByUsername("testUser")

    // Then
    assertTrue(
     retrievedUser?.hashedPassword == user.hashedPassword,
     "Expected hashed password '${user.hashedPassword}' but was '${retrievedUser?.hashedPassword}'"
    )
   }

   @Test
   fun `should store role correctly`() {
    // Given
    val expectedRole = testUser.role

    // When
    userRepository.addUser(testUser)
    val actualRole = userRepository.findByUsername("testUser")?.role

    // Then
    assertTrue(actualRole == expectedRole) {
     "Role was not stored correctly. Expected: $expectedRole, Actual: $actualRole"
    }
   }

   @Test
   fun `should not store plain text password`() {
    val password = "PlainText123!"
    val user = User(UUID.randomUUID(), "newUser", password.toMD5Hash(), Role.MATE)

    userRepository.addUser(user)
    val storedPassword = userRepository.findByUsername("newUser")?.hashedPassword

    check(storedPassword != password) {
     "Security violation: Plain text password was stored"
    }
   }

   @Test
   fun `should store correct password hash`() {
    // Given
    val plainPassword = "PlainText123!"
    val correctHash = plainPassword.toMD5Hash()
    val testUser = User(UUID.randomUUID(), "newUser", correctHash, Role.MATE)

    // When
    userRepository.addUser(testUser)
    val actualStoredHash = userRepository.findByUsername("newUser")?.hashedPassword

    // Then
    assertTrue(actualStoredHash == correctHash) {
     "Password hash was corrupted during storage. Expected: $correctHash, Got: $actualStoredHash"
    }
   }

   @Test
   fun `should return false when adding duplicate username`() {
    // Given
    val originalUser = testUser
    val duplicateUser = testUser.copy(id = UUID.randomUUID())
    userRepository.addUser(originalUser)
    // When
    val result = userRepository.addUser(duplicateUser)
    // Then
    assertFalse { result }
   }
  }
 }
}