package data.csv_parser

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import logic.models.Project
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import test_helper.toCsvData
import utilities.PROJECTS_FILE
import java.util.*

class ProjectCsvParserTest {
    private lateinit var csvHandler: CsvHandler
    private lateinit var parser: ProjectCsvParser

    @BeforeEach
    fun setUp() {
        csvHandler = mockk(relaxed = true)
        parser = ProjectCsvParser()
    }

    @Test
    fun `should return projects when parse data from project file`() {
        // Given
        val csvLines = getCsvLines()
        every { csvHandler.getLines(PROJECTS_FILE) } returns csvLines

        // When
        val result = parser.parse(csvLines)

        // Then
        val projects = getProjects()
        Truth.assertThat(result).isEqualTo(projects)
    }

    @Test
    fun `should return empty list when parse data from empty project file`() {
        // Given
        val csvLines = listOf<String>()
        every { csvHandler.getLines(PROJECTS_FILE) } returns csvLines

        // When
        val result = parser.parse(csvLines)

        // Then
        val projects = emptyList<Project>()
        Truth.assertThat(result).isEqualTo(projects)
    }

    @Test
    fun `should return empty list when parse data from empty project file with csv-header`() {
        // Given
        val csvLines = listOf(
            "id,name",
        )
        every { csvHandler.getLines(PROJECTS_FILE) } returns csvLines

        // When
        val result = parser.parse(csvLines)

        // Then
        val projects = emptyList<Project>()
        Truth.assertThat(result).isEqualTo(projects)
    }

    @Test
    fun `should return csv-data when serialize project data`() {
        // Given
        val projects = getProjects()

        // When
        val result = parser.serialize(projects)

        // Then
        val csvLines = getCsvLines().toCsvData()
        Truth.assertThat(result).isEqualTo(csvLines)
    }

    @Test
    fun `should return project header when serialize empty project data`() {
        // Given
        val projects = emptyList<Project>()

        // When
        val result = parser.serialize(projects)

        // Then
        val csvLines = "id,name"
        Truth.assertThat(result).isEqualTo(csvLines)
    }


    private fun getProjects(): List<Project> {
        return listOf(
            Project(UUID.fromString("82e16049-a9fb-4f69-b6f7-3336b68f2ae4"), "The chance", emptyList()),
            Project(UUID.fromString("045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f"), "Work", emptyList()),
            Project(UUID.fromString("07f641d4-077e-4f08-978d-3b6c9587f4bf"), "Homework", emptyList()),
        )
    }

    private fun getCsvLines(): List<String> {
        return listOf(
            "id,name",
            "82e16049-a9fb-4f69-b6f7-3336b68f2ae4,The chance",
            "045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f,Work",
            "07f641d4-077e-4f08-978d-3b6c9587f4bf,Homework",
        )
    }
}