package utilities.csv_parser

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import util.TEST_FILE

class StateCsvHandlerTest{
    private lateinit var fileGetter: FileGetter
    private lateinit var csvHandler: StateCsvHandler

    @BeforeEach
    fun setup() {
        fileGetter = mockk(relaxed = true)
        csvHandler = StateCsvHandler(fileGetter)
    }

    @Test
    fun `should call FileGetter when get file`() {
        // When
        csvHandler.getLines()

        // Then
        verify { fileGetter.getFile(TEST_FILE) }
    }
}