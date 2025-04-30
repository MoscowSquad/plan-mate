package utilities.csv_parser

import com.google.common.truth.Truth
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import util.TEST_FILE

class SourceFileGetterTest {
    private lateinit var sourceFileGetter: SourceFileGetter

    @BeforeEach
    fun setUp() {
        sourceFileGetter = SourceFileGetter()
    }

    @Test
    fun `should return the correct file when get file`() {
        // When
        val file = sourceFileGetter.getFile(TEST_FILE)

        // Then
        val fileResourceSuffix = "\\resources\\main\\$TEST_FILE"
        Truth.assertThat(file.path).endsWith(fileResourceSuffix)
    }
}