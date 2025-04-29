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
            val expected = ""
            val actual = password.toMD5Hash()
            // Then
            assertEquals(expected, actual)
        }

        @Test
        fun `should return valid hash when password has newlines, spaces or special characters`() {
            // Given
            val password = "Alternative 123 * \n \t \""
            // When
            val expected = "fcdb8862247d1b571b2715c7e8762d57"
            val actual = password.toMD5Hash()
            // Then
            assertEquals(expected, actual)
        }
    }


}