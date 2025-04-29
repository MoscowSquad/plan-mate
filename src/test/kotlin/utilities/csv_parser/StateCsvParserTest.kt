package utilities.csv_parser

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.State
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import test_helper.toCsvData
import java.util.*

class StateCsvParserTest {
    private lateinit var csvReader: StateCsvReader
    private lateinit var csvWriter: StateCsvWriter
    private lateinit var parser: StateCsvParser

    @BeforeEach
    fun setUp() {
        csvReader = mockk(relaxed = true)
        csvWriter = mockk(relaxed = true)
        parser = StateCsvParser(csvReader, csvWriter)
    }

    @Test
    fun `should call StateCsvReader when parsing states data`() {
        parser.parse()
        verify { csvReader.getLines() }
    }

    @Test
    fun `should call StateCsvWriter when serialize states data`() {
        parser.serialize(emptyList())
        verify { csvWriter.write("") }
    }


    @Test
    fun `should return states when parse data from state file`() {
        // Given
        val csvLines = getCsvLines()
        every { csvReader.getLines() } returns csvLines

        // When
        val result = parser.parse()

        // Then
        val states = getStates()
        Truth.assertThat(result).isEqualTo(states)
    }

    @Test
    fun `should return empty list when parse data from empty state file`() {
        // Given
        val csvLines = listOf<String>()
        every { csvReader.getLines() } returns csvLines

        // When
        val result = parser.parse()

        // Then
        val states = emptyList<State>()
        Truth.assertThat(result).isEqualTo(states)
    }

    @Test
    fun `should return empty list when parse data from empty state file with csv-header`() {
        // Given
        val csvLines = listOf(
            "id,title,projectId",
        )
        every { csvReader.getLines() } returns csvLines

        // When
        val result = parser.parse()

        // Then
        val states = emptyList<State>()
        Truth.assertThat(result).isEqualTo(states)
    }

    @Test
    fun `should return csv-data when serialize state data`() {
        // Given
        val states = getStates()

        // When
        val result = parser.serialize(states)

        // Then
        val csvLines = getCsvLines().toCsvData()
        Truth.assertThat(result).isEqualTo(csvLines)
    }

    @Test
    fun `should return state header when serialize empty state data`() {
        // Given
        val states = emptyList<State>()

        // When
        val result = parser.serialize(states)

        // Then
        val csvLines = "id,title,projectId"
        Truth.assertThat(result).isEqualTo(csvLines)
    }


    private fun getStates(): List<State> {
        return listOf<State>(
            createState("82e16049-a9fb-4f69-b6f7-3336b68f2ae4", "Todo", "w09w98we-d23d-4f69-b6f7-3336b68f2ae4"),
            createState("045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f", "In Progress", "8223k433-3l3l-23j0-b6f7-3336b68f2ae4"),
            createState("07f641d4-077e-4f08-978d-3b6c9587f4bf", "Done", "9283h32p-o320-lk30-b6f7-3336b68f2ae4"),
        )
    }

    private fun getCsvLines(): List<String> {
        return listOf(
            "id,title,projectId",
            "82e16049-a9fb-4f69-b6f7-3336b68f2ae4,The chance,w09w98we-d23d-4f69-b6f7-3336b68f2ae4",
            "045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f,Work,8223k433-3l3l-23j0-b6f7-3336b68f2ae4",
            "07f641d4-077e-4f08-978d-3b6c9587f4bf,Homework,9283h32p-o320-lk30-b6f7-3336b68f2ae4",
        )
    }


    private fun createState(
        id: String,
        title: String,
        projectId: String,
    ): State {
        return State(
            UUID.fromString(id), title, UUID.fromString(projectId),
        )
    }
}