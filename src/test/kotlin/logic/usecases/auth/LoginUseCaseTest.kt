package logic.usecases.auth

import logic.util.toMD5Hash
import logic.models.User
import logic.repositories.AuthenticationRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LoginUseCaseTest {

 private class FakeAuthRepository : AuthenticationRepository {
  var shouldSucceed = true
  var lastHashedPassword: String? = null
  var lastUsername: String? = null

  override fun register(user: User) = user
  override fun login(name: String, password: String): Boolean {
   lastUsername = name
   lastHashedPassword = password
   return shouldSucceed
  }
 }

 private val fakeRepository = FakeAuthRepository()
 private val loginUseCase = LoginUseCase(fakeRepository)

 @Test
 fun `should return true for successful authentication`() {
  fakeRepository.shouldSucceed = true
  assertTrue(loginUseCase("validUser", "correctPassword"))
 }

 @Test
 fun `should return false for failed authentication`() {
  fakeRepository.shouldSucceed = false
  assertFalse(loginUseCase("validUser", "wrongPassword"))
 }

 @Test
 fun `should hash password with MD5 before authentication`() {
  loginUseCase("testUser", "test123")
  assertEquals("test123".toMD5Hash(), fakeRepository.lastHashedPassword)
 }

 @Test
 fun `should throw when username is blank`() {
  val exception = assertThrows<IllegalArgumentException> {
   loginUseCase("", "password")
  }
  assertTrue(exception.message!!.contains("Username cannot be blank"))
 }

 @Test
 fun `should throw when password is blank`() {
  val exception = assertThrows<IllegalArgumentException> {
   loginUseCase("username", "")
  }
  assertTrue(exception.message!!.contains("Password cannot be blank"))
 }

 @Test
 fun `should pass username unchanged to repository`() {
  val testUsername = "testUser123"
  fakeRepository.shouldSucceed = true
  loginUseCase(testUsername, "password")
  assertEquals(testUsername, fakeRepository.lastUsername)
 }
}