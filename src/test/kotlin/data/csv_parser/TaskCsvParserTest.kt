package data.csv_parser

import com.google.common.truth.Truth
import data.dto.TaskDto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class TaskCsvParserTest {
    private lateinit var parser: TaskCsvParser

    @BeforeEach
    fun setUp() {
        parser = TaskCsvParser()
    }


    @Test
    fun `should return tasks when parse data from task file`() {
        // Given
        val csvLines = getCsvLines()

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

        // When
        val result = parser.parse(csvLines)

        // Then
        val tasks = emptyList<TaskDto>()
        Truth.assertThat(result).isEqualTo(tasks)
    }

    @Test
    fun `should return empty list when parse data from empty task file with csv-header`() {
        // Given
        val csvLines = listOf(
            "id,title,description,projectId,stateId",
        )

        // When
        val result = parser.parse(csvLines)

        // Then
        val tasks = emptyList<TaskDto>()
        Truth.assertThat(result).isEqualTo(tasks)
    }

    @Test
    fun `should return csv-data when serialize task data`() {
        // Given
        val tasks = getTasks()

        // When
        val result = parser.serialize(tasks)

        // Then
        val csvLines = getCsvLines()
        Truth.assertThat(result).isEqualTo(csvLines)
    }

    @Test
    fun `should return task header when serialize empty task data`() {
        // Given
        val tasks = emptyList<TaskDto>()

        // When
        val result = parser.serialize(tasks)

        // Then
        val csvLines = listOf("id,title,description,projectId,stateId")
        Truth.assertThat(result).isEqualTo(csvLines)
    }


    private fun getTasks(): List<TaskDto> {
        return listOf(
            createTask(
                "6d3c0dfc-5b05-479d-be86-4d31b22582cc",
                "Video 101 to 120",
                "",
                "6d3c0dfc-5b05-479d-be86-4d31b22582cc",
                "6d3c0dfc-5b05-479d-be86-4d31b22582cc"
            ),
            createTask(
                "6d3c0dfc-5b05-479d-be86-4d31b22582cc",
                "Video 201 to 220",
                "Don't watch video N0.203",
                "6d3c0dfc-5b05-479d-be86-4d31b22582cc",
                "6d3c0dfc-5b05-479d-be86-4d31b22582cc"
            ),
            createTask(
                "6d3c0dfc-5b05-479d-be86-4d31b22582cc",
                "Video 301 to 320",
                "",
                "6d3c0dfc-5b05-479d-be86-4d31b22582cc",
                "6d3c0dfc-5b05-479d-be86-4d31b22582cc"
            ),
        )
    }

    private fun getCsvLines(): List<String> {
        return listOf(
            "id,title,description,projectId,stateId",
            "6d3c0dfc-5b05-479d-be86-4d31b22582cc,Video 101 to 120,,6d3c0dfc-5b05-479d-be86-4d31b22582cc,6d3c0dfc-5b05-479d-be86-4d31b22582cc",
            "6d3c0dfc-5b05-479d-be86-4d31b22582cc,Video 201 to 220,Don't watch video N0.203,6d3c0dfc-5b05-479d-be86-4d31b22582cc,6d3c0dfc-5b05-479d-be86-4d31b22582cc",
            "6d3c0dfc-5b05-479d-be86-4d31b22582cc,Video 301 to 320,,6d3c0dfc-5b05-479d-be86-4d31b22582cc,6d3c0dfc-5b05-479d-be86-4d31b22582cc",
        )
    }

    private fun createTask(
        id: String,
        title: String,
        description: String,
        projectId: String,
        stateId: String,
    ): TaskDto = TaskDto(id, title, description, projectId, stateId)
}