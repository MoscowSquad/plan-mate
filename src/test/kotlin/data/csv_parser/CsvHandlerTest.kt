package data.csv_parser

import com.google.common.truth.Truth
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import utilities.TEST_FILE
import java.io.File

class CsvHandlerTest {
    private lateinit var file: File
    private lateinit var csvHandler: CsvHandler

    @BeforeEach
    fun setup() {
        file = File(requireNotNull(javaClass.classLoader.getResource(TEST_FILE)).toURI())
        csvHandler = CsvHandler(file)
    }

    @Test
    fun `should return a list of lines when get file content`() {
        // Given
        val fileContent = getTestText()
        file.writeText(fileContent)

        // When
        val result = csvHandler.getLines()

        // Then
        val lines = getTestLines()
        Truth.assertThat(result).isEqualTo(lines)
    }

    @Test
    fun `should return a list of lines without the empty lines when get file content`() {
        // Given
        val fileContent = "\n\n\n\n" + getTestText() + "\n\n\n\n"
        file.writeText(fileContent)

        // When
        val result = csvHandler.getLines()

        // Then
        val expectedLines = getTestLines()
        Truth.assertThat(result).isEqualTo(expectedLines)
    }

    @Test
    fun `should return a list of lines without the blank lines when get file content`() {
        // Given
        val fileContent = "\n   \n  \n \t\n" + getTestText() + " \n\t\n \n      \n"
        file.writeText(fileContent)

        // When
        val result = csvHandler.getLines()

        // Then
        val expectedLines = getTestLines()
        Truth.assertThat(result).isEqualTo(expectedLines)
    }

    @Test
    fun `should write content to file when list of strings is provided`() {
        // Given
        val content = listOf("Test1", "Test2", "Test3", "Test4")

        // When
        csvHandler.write(content)

        // Then
        val fileContent = file.readText()
        val expectedContent = "Test1\nTest2\nTest3\nTest4\n"
        Truth.assertThat(fileContent).isEqualTo(expectedContent)
    }

    @Test
    fun `should write content to file when a list of non-empty strings is provided`() {
        // Given
        val content = listOf("Test1", "Test2", "", "Test3", "Test4", "", "")

        // When
        csvHandler.write(content)

        // Then
        val fileContent = file.readText()
        val expectedContent = "Test1\nTest2\nTest3\nTest4\n"
        Truth.assertThat(fileContent).isEqualTo(expectedContent)
    }

    @Test
    fun `should write content to file when a list of non-blank strings is provided`() {
        // Given
        val content = listOf("Test1", "    ", "Test2", "    ", "Test3", "Test4", "     ")

        // When
        csvHandler.write(content)

        // Then
        val fileContent = file.readText()
        val expectedContent = "Test1\nTest2\nTest3\nTest4\n"
        Truth.assertThat(fileContent).isEqualTo(expectedContent)
    }

    @Test
    fun `should write content to file when a list of non-space characters strings is provided`() {
        // Given
        val content = listOf("Test1", "\n", "Test2", "\t", "Test3", "Test4", "\n")

        // When
        csvHandler.write(content)

        // Then
        val fileContent = file.readText()
        val expectedContent = "Test1\nTest2\nTest3\nTest4\n"
        Truth.assertThat(fileContent).isEqualTo(expectedContent)
    }

    private fun getTestLines(): List<String> {
        return listOf(
            "This is line 1",
            "This is line 2",
            "This is line 3",
            "This is line 4",
            "This is line 5",
            "This is line 6",
        )
    }

    private fun getTestText(): String {
        return "This is line 1\n" +
                "This is line 2\n" +
                "This is line 3\n" +
                "This is line 4\n" +
                "This is line 5\n" +
                "This is line 6\n"
    }
}