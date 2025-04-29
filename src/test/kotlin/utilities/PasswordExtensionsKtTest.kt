package utilities

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PasswordExtensionsKtTest {
    @Nested
    inner class ToMD5HashTests {
        @Test
        fun `should return a valid hash string when valid password entered`() {
            // Given
            val password = "Alternative123*"
            // When
            val expected = "f834f1ba5e63f744bfab1d5c4b787d4e"
            val actual = password.toMD5Hash()
            // Then
            assertEquals(expected, actual)
        }

        @Test
        fun `should return empty hash string when empty password entered`() {
            // Given
            val password = ""
            // When
            val expected = "d41d8cd98f00b204e9800998ecf8427e"
            val actual = password.toMD5Hash()
            // Then
            assertEquals(expected, actual)
        }

        @Test
        fun `should return valid hash when password has spaces or special characters`() {
            // Given
            val password = "Alternative# * @ % ! ^"
            // When
            val expected = "a31c7a3f2f3f1f059bf19c6e97ee9918"
            val actual = password.toMD5Hash()
            // Then
            assertEquals(expected, actual)
        }
    }

    @Nested
    inner class PasswordFormatValidationTests {
        @Test
        fun `should return false when password is less than 8 characters`() {
            // Given
            val password = "Alt12*"
            // When
            val actual = password.isValidPasswordFormat()
            // Then
            assertFalse { actual }
        }

        @Test
        fun `should return false when password not have at least one small letter`() {
            // Given
            val password = "ALTERNATIVE#I12"
            // When
            val actual = password.isValidPasswordFormat()
            // Then
            assertFalse { actual }
        }

        @Test
        fun `should return false when password not have at least one capital letter`() {
            // Given
            val password = "alternative12*"
            // When
            val actual = password.isValidPasswordFormat()
            // Then
            assertFalse { actual }
        }

        @Test
        fun `should return false when password not have at least one symbol`() {
            // Given
            val password = "Alternative1"
            // When
            val actual = password.isValidPasswordFormat()
            // Then
            assertFalse { actual }
        }

        @Test
        fun `should return false when password not have at least one number letter`() {
            // Given
            val password = "Alternative#"
            // When
            val actual = password.isValidPasswordFormat()
            // Then
            assertFalse { actual }
        }

        @Test
        fun `should return true when the password is valid`() {
            // Given
            val password = "Alternative123#"
            // When
            val actual = password.isValidPasswordFormat()
            // Then
            assertTrue { actual }
        }
    }

    @Nested
    inner class HashMatchingTests {
        @Test
        fun `should return false when hashes don't match`() {
            // Given
            val hash = "f834f1ba5e63f744bfab1d5c4b787d4e"
            // When
            val actual = hash.matchesMD5Hash("fcdb8862247d1b571b2715c7e8762d57")
            // Then
            assertFalse { actual }
        }

        @Test
        fun `should return true when matching empty hashes`() {
            // Given
            val hash = ""
            // When
            val actual = hash.matchesMD5Hash("")
            // Then
            assertTrue { actual }
        }

        @Test
        fun `should return true when hashes match`() {
            // Given
            val hash = "f834f1ba5e63f744bfab1d5c4b787d4e"
            // When
            val actual = hash.matchesMD5Hash("f834f1ba5e63f744bfab1d5c4b787d4e")
            // Then
            assertTrue { actual }
        }
    }

}