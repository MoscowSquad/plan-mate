import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.util.*
import logic.models.Role
import logic.models.User
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
  fun `should reject empty username`() {
   // Given
   val input = ""

   // When
   val result = validateUsername(input)

   // Then
   assertFalse(result.isValid)
   assertEquals("Username cannot be empty", result.errorMessage)
  }

  @Test
  fun `should reject username shorter than 3 characters`() {
   // Given
   val input = "ab"

   // When
   val result = validateUsername(input)

   // Then
   assertFalse(result.isValid)
   assertEquals("Username must be at least 3 characters", result.errorMessage)
  }

  @Test
  fun `should reject username longer than 20 characters`() {
   // Given
   val input = "a".repeat(21)

   // When
   val result = validateUsername(input)

   // Then
   assertFalse(result.isValid)
   assertEquals("Username cannot exceed 20 characters", result.errorMessage)
  }

  @Test
  fun `should reject username with spaces`() {
   // Given
   val input = "user name"

   // When
   val result = validateUsername(input)

   // Then
   assertFalse(result.isValid)
   assertEquals("Username cannot contain spaces", result.errorMessage)
  }

  @Test
  fun `should reject username with special characters`() {
   // Given
   val input = "user@name"

   // When
   val result = validateUsername(input)

   // Then
   assertFalse(result.isValid)
   assertEquals("Username can only contain alphanumeric characters and underscores", result.errorMessage)
  }

  @Test
  fun `should accept valid username`() {
   // Given
   val validUsernames = listOf("user123", "test_user", "normalUser")

   validUsernames.forEach { username ->
    // When
    val result = validateUsername(username)

    // Then
    assertTrue(result.isValid)
    assertNull(result.errorMessage)
   }
  }
 }

 @Nested
 inner class PasswordValidationTests {
  @Test
  fun `should reject password shorter than 8 characters`() {
   // Given
   val password = "Pass1!"

   // When & Then
   assertFalse(password.isValidPasswordFormat())
  }

  @Test
  fun `should reject password without uppercase letter`() {
   // Given
   val password = "password123!"

   // When & Then
   assertFalse(password.isValidPasswordFormat())
  }

  @Test
  fun `should reject password without lowercase letter`() {
   // Given
   val password = "PASSWORD123!"

   // When & Then
   assertFalse(password.isValidPasswordFormat())
  }

  @Test
  fun `should reject password without number`() {
   // Given
   val password = "Password!"

   // When & Then
   assertFalse(password.isValidPasswordFormat())
  }

  @Test
  fun `should reject password without special character`() {
   // Given
   val password = "Password123"

   // When & Then
   assertFalse(password.isValidPasswordFormat())
  }

  @Test
  fun `should accept valid password`() {
   // Given
   val password = "ValidPass123!"

   // When & Then
   assertTrue(password.isValidPasswordFormat())
  }

  @Test
  fun `should hash password correctly`() {
   // Given
   val password = "Test123!"

   // When
   val hashed = password.toMD5Hash()

   // Then
   assertNotEquals(password, hashed)
   assertEquals(32, hashed.length) // MD5 produces 32-character hex string
  }
 }

 @Nested
 inner class LoginValidationTests {
  private val userRepository = FakeUserRepository().apply {
   addUser(testUser)
   addUser(testAdmin)
  }

  @Test
  fun `should fail login with incorrect username`() {
   // Given
   val username = "wrongUser"
   val password = "Test123!"

   // When
   val result = validateLogin(username, password, userRepository)

   // Then
   assertFalse(result.isValid)
   assertEquals("Invalid username or password", result.errorMessage)
  }

  @Test
  fun `should fail login with incorrect password`() {
   // Given
   val username = "testUser"
   val password = "wrongPass"

   // When
   val result = validateLogin(username, password, userRepository)

   // Then
   assertFalse(result.isValid)
   assertEquals("Invalid username or password", result.errorMessage)
  }

  @Test
  fun `should succeed login with correct credentials`() {
   // Given
   val username = "testUser"
   val password = "Test123!"

   // When
   val result = validateLogin(username, password, userRepository)

   // Then
   assertTrue(result.isValid)
   assertNull(result.errorMessage)
  }

  @Test
  fun `should return user role after successful login`() {
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
  fun `should store user information correctly`() {
   // Given
   val userToStore = testUser

   // When
   userRepository.addUser(userToStore)
   val retrievedUser = userRepository.findByUsername("testUser")

   // Then
   assertNotNull(retrievedUser)
   assertEquals(userToStore.id, retrievedUser?.id)
   assertEquals(userToStore.username, retrievedUser?.username)
   assertEquals(userToStore.hashedPassword, retrievedUser?.hashedPassword)
   assertEquals(userToStore.role, retrievedUser?.role)
  }

  @Test
  fun `should not store plain text passwords`() {
   // Given
   val password = "PlainText123!"
   val user = User(UUID.randomUUID(), "newUser", password.toMD5Hash(), Role.MATE)

   // When
   userRepository.addUser(user)
   val storedUser = userRepository.findByUsername("newUser")

   // Then
   assertNotEquals(password, storedUser?.hashedPassword)
   assertEquals(password.toMD5Hash(), storedUser?.hashedPassword)
  }

  @Test
  fun `should not allow duplicate usernames`() {
   // Given
   val originalUser = testUser
   val duplicateUser = testUser.copy(id = UUID.randomUUID())
   userRepository.addUser(originalUser)

   // When
   val result = userRepository.addUser(duplicateUser)

   // Then
   assertFalse(result, "Duplicate username should not be allowed")
  }
 }
}