package data.csv_parser

import com.google.common.truth.Truth
import data.csv_data.csv_parser.CsvHandler
import test_helper.getTestLines
import test_helper.getTestText
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class CsvHandlerTest {
    private lateinit var file: File
    private lateinit var csvHandler: CsvHandler

    @BeforeEach
    fun setup() {
        file = File.createTempFile("test_csv", ".csv")
        file.deleteOnExit()
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
    fun `should create file if not exist when write file data`() {
        // Given
        val content = listOf("Test1", "Test2", "Test3", "Test4")
        file.delete()

        // When
        csvHandler.write(content)

        // Then
        Truth.assertThat(file.exists()).isTrue()
    }

    @Test
    fun `should create file if not exist when get data as lines`() {
        // Given
        file.delete()

        // When
        val result = csvHandler.getLines()

        // Then
        Truth.assertThat(file.exists()).isTrue()
        Truth.assertThat(result).isEmpty()
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

    @Test
    fun `should return empty list when file is empty`() {
        // Given
        file.writeText("")

        // When
        val result = csvHandler.getLines()

        // Then
        Truth.assertThat(result).isEmpty()
    }
}