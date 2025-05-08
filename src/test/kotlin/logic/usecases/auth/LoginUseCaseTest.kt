package logic.usecases.auth

import logic.util.UserNotFoundException
import logic.util.toMD5Hash
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoginUseCaseTest {

    private lateinit var loginUseCase: LoginUseCase
    private lateinit var usersFile: File

    @BeforeEach
    fun setUp(@TempDir tempDir: Path) {
        usersFile = File(tempDir.toFile(), "users.csv")
        usersFile.writeText(
            """
            id,username,password,role
            1,validUser,${"correctPassword".toMD5Hash()},ADMIN
            2,testUser,${"test123".toMD5Hash()},MATE
        """.trimIndent()
        )

        loginUseCase = LoginUseCase()
        val field = LoginUseCase::class.java.getDeclaredField("usersFile")
        field.isAccessible = true
        field.set(loginUseCase, usersFile)
    }

    @Test
    fun `should return true for successful authentication`() {
        assertTrue(loginUseCase("validUser", "correctPassword"))
    }

    @Test
    fun `should throw UserNotFoundException for failed authentication`() {
        assertThrows<UserNotFoundException> {
            loginUseCase("validUser", "wrongPassword")
        }
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
    fun `should ignore case for username comparison`() {
        assertTrue(loginUseCase("VALIDUSER", "correctPassword"))
    }

    @Test
    fun `should throw when users file does not exist`() {
        // Delete the users file
        usersFile.delete()

        val exception = assertThrows<IllegalStateException> {
            loginUseCase("validUser", "correctPassword")
        }
        assertTrue(exception.message!!.contains("users.csv not found"))
    }

    @Test
    fun `should throw UserNotFoundException when user does not exist`() {
        val exception = assertThrows<UserNotFoundException> {
            loginUseCase("nonExistentUser", "anyPassword")
        }
        assertEquals("User 'nonExistentUser' does not exist", exception.message)
    }
    @Test
    fun `should handle malformed CSV lines without enough columns`() {
        usersFile.writeText(
            """
        id,username,password,role
        1,validUser,${"correctPassword".toMD5Hash()},ADMIN
        2,testUser
        """.trimIndent()
        )

        assertTrue(loginUseCase("validUser", "correctPassword"))

        val exception = assertThrows<UserNotFoundException> {
            loginUseCase("testUser", "anyPassword")
        }
        assertEquals("User 'testUser' does not exist", exception.message)
    }
}