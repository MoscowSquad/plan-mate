import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import logic.models.Role
import logic.models.User
import logic.repositoies.AuthenticationRepository
import logic.usecases.AuthenticationUseCase
import org.junit.Assert.*
import utilities.isValidPasswordFormat
import utilities.toMD5Hash


class UserAuthenticationTest {
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

 private val testRepository = object : AuthenticationRepository {
  override val users = mutableListOf<User>()

  override fun addUser(user: User) {  // Explicit Unit return
   users.add(user)
  }

  override fun findByUsername(username: String): User? {
   return users.find { it.username == username }
  }
 }
 private val useCase = AuthenticationUseCase(testRepository)

 @Nested
 inner class UsernameValidationTests {
  @Test
  fun `should throw IllegalArgumentException when username is empty`() {
   assertThrows<IllegalArgumentException> { useCase.validateUsername("") }
  }

  @Test
  fun `should return false for invalid usernames`() {
   listOf(
    "ab",               // too short
    "a".repeat(21),      // too long
    "user name",        // contains spaces
    "testUser"          // existing username (after adding)
   ).forEach { username ->
    if (username == "testUser") testRepository.addUser(testUser)
    assertFalse(useCase.validateUsername(username))
   }
  }

  @Test
  fun `should accept valid username`() {
   assertTrue(useCase.validateUsername("valid_username"))
  }
 }

 @Nested
 inner class PasswordValidationTests {
  @Test
  fun `should validate password format`() {
   mapOf(
    "short" to false,             // too short
    "nouppercase123!" to false,  // no uppercase
    "ValidPass123!" to true,      // valid
    "MissingSpecial123" to false  // no special chars
   ).forEach { (password, expected) ->
    assertEquals(expected, password.isValidPasswordFormat())
   }
  }
 }

 @Nested
 inner class LoginValidationTests {
  init {
   testRepository.addUser(testUser)
   testRepository.addUser(testAdmin)
  }

  @Test
  fun `should authenticate users`() {
   mapOf(
    Pair("wrongUser", "wrongPass") to false,
    Pair("testUser", "Test123!") to true
   ).forEach { (credentials, expected) ->
    assertEquals(expected, useCase.validateLogin(credentials.first, credentials.second))
   }
  }
 }

 @Nested
 inner class UserStorageTests {
  @Test
  fun `should store user and return non-null user object`() {
   // Given
   val testUser = createTestUser()

   // When
   testRepository.addUser(testUser)
   val storedUser = testRepository.findByUsername("testUser")

   // Then
   assertThat(storedUser).isNotNull()
  }

  @Test
  fun `should store correct username`() {
   // Given
   val testUser = createTestUser()
   testRepository.addUser(testUser)

   // When
   val storedUser = testRepository.findByUsername("testUser")

   // Then
   assertThat(storedUser?.username).isEqualTo("testUser")
  }

  @Test
  fun `should store hashed password, not plain text`() {
   // Given
   val testUser = createTestUser()
   testRepository.addUser(testUser)

   // When
   val storedUser = testRepository.findByUsername("testUser")

   // Then
   assertThat(storedUser?.hashedPassword).isNotEqualTo("Test123!")
  }

  @Test
  fun `should store correct user role`() {
   // Given
   val testUser = createTestUser()
   testRepository.addUser(testUser)

   // When
   val storedUser = testRepository.findByUsername("testUser")

   // Then
   assertThat(storedUser?.role).isEqualTo(Role.MATE)
  }

  @Test
  fun `should prevent duplicate username registration`() {
   // Given
   val testUser = createTestUser()
   testRepository.addUser(testUser)

   // When & Then
   assertThrows<IllegalStateException> {
    testRepository.addUser(testUser.copy(id = UUID.randomUUID()))
   }
  }

  private fun createTestUser() = User(
   id = UUID.randomUUID(),
   username = "testUser",
   hashedPassword = "Test123!".toMD5Hash(),
   role = Role.MATE
  )
  }
 }