package data.csv_parser

import com.google.common.truth.Truth
import data.csv_data.csv_parser.CsvHandler
import data.csv_data.csv_parser.ProjectCsvParser
import data.csv_data.dto.ProjectDto
import test_helper.getProjectCsvLines
import test_helper.getProjects
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
        val csvLines = getProjectCsvLines()
        every { csvHandler.getLines() } returns csvLines

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
        every { csvHandler.getLines() } returns csvLines

        // When
        val result = parser.parse(csvLines)

        // Then
        val projects = emptyList<ProjectDto>()
        Truth.assertThat(result).isEqualTo(projects)
    }

    @Test
    fun `should return empty list when parse data from empty project file with csv-header`() {
        // Given
        val csvLines = listOf(
            "id,name",
        )
        every { csvHandler.getLines() } returns csvLines

        // When
        val result = parser.parse(csvLines)

        // Then
        val projects = emptyList<ProjectDto>()
        Truth.assertThat(result).isEqualTo(projects)
    }

    @Test
    fun `should return csv-data when serialize project data`() {
        // Given
        val projects = getProjects()

        // When
        val result = parser.serialize(projects)

        // Then
        val csvLines = getProjectCsvLines()
        Truth.assertThat(result).isEqualTo(csvLines)
    }

    @Test
    fun `should return project header when serialize empty project data`() {
        // Given
        val projects = emptyList<ProjectDto>()

        // When
        val result = parser.serialize(projects)

        // Then
        val csvLines = listOf("id,name")
        Truth.assertThat(result).isEqualTo(csvLines)
    }
}