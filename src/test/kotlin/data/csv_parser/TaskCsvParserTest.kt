package data.csv_parser

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.Task
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import test_helper.toCsvData
import java.util.*

class TaskCsvParserTest {
    private lateinit var csvHandler: CsvHandler
    private lateinit var parser: TaskCsvParser

    @BeforeEach
    fun setUp() {
        csvHandler = mockk(relaxed = true)
        parser = TaskCsvParser(csvHandler)
    }

    @Test
    fun `should call TaskCsvHandler when parsing tasks data`() {
        parser.parse(emptyList())
        verify { csvHandler.getLines() }
    }

    @Test
    fun `should call TaskCsvHandler when serialize tasks data`() {
        parser.serialize(emptyList())
        verify { csvHandler.write(emptyList()) }
    }


    @Test
    fun `should return tasks when parse data from task file`() {
        // Given
        val csvLines = getCsvLines()
        every { csvHandler.getLines() } returns csvLines

        // When
        val result = parser.parse(csvLines)

        // Then
        val tasks = getTasks()
        Truth.assertThat(result).isEqualTo(tasks)
    }

    @Test
    fun `should return empty list when parse data from empty task file`() {
        // Given
        val csvLines = listOf<String>()
        every { csvHandler.getLines() } returns csvLines

        // When
        val result = parser.parse(csvLines)

        // Then
        val tasks = emptyList<Task>()
        Truth.assertThat(result).isEqualTo(tasks)
    }

    @Test
    fun `should return empty list when parse data from empty task file with csv-header`() {
        // Given
        val csvLines = listOf(
            "id,title,projectId",
        )
        every { csvHandler.getLines() } returns csvLines

        // When
        val result = parser.parse(csvLines)

        // Then
        val tasks = emptyList<Task>()
        Truth.assertThat(result).isEqualTo(tasks)
    }

    @Test
    fun `should return csv-data when serialize task data`() {
        // Given
        val tasks = getTasks()

        // When
        val result = parser.serialize(tasks)

        // Then
        val csvLines = getCsvLines().toCsvData()
        Truth.assertThat(result).isEqualTo(csvLines)
    }

    @Test
    fun `should return task header when serialize empty task data`() {
        // Given
        val tasks = emptyList<Task>()

        // When
        val result = parser.serialize(tasks)

        // Then
        val csvLines = "id,title,projectId"
        Truth.assertThat(result).isEqualTo(csvLines)
    }


    private fun getTasks(): List<Task> {
        return listOf(
            createTask(
                "82e16049-a9fb-4f69-b6f7-3336b68f2ae4",
                "Video 101 to 120",
                "",
                "w09w98we-d23d-4f69-b6f7-3336b68f2ae4",
                "w09w98we-d23d-4f69-b6f7-3336b68f2ae4"
            ),
            createTask(
                "045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f",
                "Video 201 to 220",
                "Don't watch video N0.203",
                "8223k433-3l3l-23j0-b6f7-3336b68f2ae4",
                "lk342l33-3l3l-0923-23l0-23k0i32k3303"
            ),
            createTask(
                "07f641d4-077e-4f08-978d-3b6c9587f4bf",
                "Video 301 to 320",
                "",
                "9283h32p-o320-lk30-b6f7-3336b68f2ae4",
                "ki33h32p-3ij3-lk30-k303-k3i03j39j030"
            ),
        )
    }

    private fun getCsvLines(): List<String> {
        return listOf(
            "id,title,description,projectId,stateId",
            "82e16049-a9fb-4f69-b6f7-3336b68f2ae4,Video 101 to 120,,w09w98we-d23d-4f69-b6f7-3336b68f2ae4,w09w98we-d23d-4f69-b6f7-3336b68f2ae4",
            "045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f,Video 201 to 220,Don't watch video N0.203,8223k433-3l3l-23j0-b6f7-3336b68f2ae4,lk342l33-3l3l-0923-23l0-23k0i32k3303",
            "07f641d4-077e-4f08-978d-3b6c9587f4bf,Video 301 to 320,,9283h32p-o320-lk30-b6f7-3336b68f2ae4,ki33h32p-3ij3-lk30-k303-k3i03j39j030",
        )
    }

    private fun createTask(
        id: String,
        title: String,
        description: String,
        projectId: String,
        stateId: String,
    ): Task = Task(UUID.fromString(id), title, description, UUID.fromString(projectId), UUID.fromString(stateId))
}