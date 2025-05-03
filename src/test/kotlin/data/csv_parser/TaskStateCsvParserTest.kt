package data.csv_parser

import com.google.common.truth.Truth
import data.dto.TaskStateDto
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TaskStateCsvParserTest {
    private lateinit var csvHandler: CsvHandler
    private lateinit var parser: TaskStateCsvParser

    @BeforeEach
    fun setUp() {
        csvHandler = mockk(relaxed = true)
        parser = TaskStateCsvParser()
    }

    @Test
    fun `should return states when parse data from state file`() {
        // Given
        val csvLines = getCsvLines()
        every { csvHandler.getLines() } returns csvLines

        // When
        val result = parser.parse(csvLines)

        // Then
        val states = getStates()
        Truth.assertThat(result).isEqualTo(states)
    }

    @Test
    fun `should return empty list when parse data from empty state file`() {
        // Given
        val csvLines = listOf<String>()
        every { csvHandler.getLines() } returns csvLines

        // When
        val result = parser.parse(csvLines)

        // Then
        val states = emptyList<TaskStateDto>()
        Truth.assertThat(result).isEqualTo(states)
    }

    @Test
    fun `should return empty list when parse data from empty state file with csv-header`() {
        // Given
        val csvLines = listOf("id,title,projectId")
        every { csvHandler.getLines() } returns csvLines

        // When
        val result = parser.parse(csvLines)

        // Then
        val states = emptyList<TaskStateDto>()
        Truth.assertThat(result).isEqualTo(states)
    }

    @Test
    fun `should return csv-data when serialize state data`() {
        // Given
        val states = getStates()

        // When
        val result = parser.serialize(states)

        // Then
        val csvLines = getCsvLines()
        Truth.assertThat(result).isEqualTo(csvLines)
    }

    @Test
    fun `should return state header when serialize empty state data`() {
        // Given
        val states = emptyList<TaskStateDto>()

        // When
        val result = parser.serialize(states)

        // Then
        val csvLines = listOf("id,title,projectId")
        Truth.assertThat(result).isEqualTo(csvLines)
    }


    private fun getStates(): List<TaskStateDto> {
        return listOf(
            createState("caf22bb1-90ff-409d-bef2-3b8bc9759354", "Todo", "caf22bb1-90ff-409d-bef2-3b8bc9759354"),
            createState("caf22bb1-90ff-409d-bef2-3b8bc9759354", "In Progress", "caf22bb1-90ff-409d-bef2-3b8bc9759354"),
            createState("caf22bb1-90ff-409d-bef2-3b8bc9759354", "Done", "caf22bb1-90ff-409d-bef2-3b8bc9759354"),
        )
    }

    private fun getCsvLines(): List<String> {
        return listOf(
            "id,title,projectId",
            "caf22bb1-90ff-409d-bef2-3b8bc9759354,Todo,caf22bb1-90ff-409d-bef2-3b8bc9759354",
            "caf22bb1-90ff-409d-bef2-3b8bc9759354,In Progress,caf22bb1-90ff-409d-bef2-3b8bc9759354",
            "caf22bb1-90ff-409d-bef2-3b8bc9759354,Done,caf22bb1-90ff-409d-bef2-3b8bc9759354",
        )
    }


    private fun createState(
        id: String,
        title: String,
        projectId: String,
    ): TaskStateDto {
        return TaskStateDto(id, title, projectId)
    }
}