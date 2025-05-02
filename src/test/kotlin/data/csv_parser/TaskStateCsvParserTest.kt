package data.csv_parser

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import logic.models.TaskState
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import test_helper.toCsvData
import utilities.STATES_FILE
import java.util.*

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
        every { csvHandler.getLines(STATES_FILE) } returns csvLines

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
        every { csvHandler.getLines(STATES_FILE) } returns csvLines

        // When
        val result = parser.parse(csvLines)

        // Then
        val states = emptyList<TaskState>()
        Truth.assertThat(result).isEqualTo(states)
    }

    @Test
    fun `should return empty list when parse data from empty state file with csv-header`() {
        // Given
        val csvLines = listOf(
            "id,title,projectId",
        )
        every { csvHandler.getLines(STATES_FILE) } returns csvLines

        // When
        val result = parser.parse(csvLines)

        // Then
        val states = emptyList<TaskState>()
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
        val states = emptyList<TaskState>()

        // When
        val result = parser.serialize(states)

        // Then
        val csvLines = "id,title,projectId"
        Truth.assertThat(result).isEqualTo(csvLines)
    }


    private fun getStates(): List<TaskState> {
        return listOf(
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
    ): TaskState {
        return TaskState(
            UUID.fromString(id), title, UUID.fromString(projectId),
        )
    }
}