package data.csv_parser

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import data.dto.ProjectDto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
        val csvLines = getCsvLines()
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


    private fun getProjects(): List<ProjectDto> {
        return listOf(
            createProject("045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f", "The chance"),
            createProject("045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f", "Work"),
            createProject("045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f", "Homework"),
        )
    }

    private fun createProject(id: String, name: String): ProjectDto {
        return ProjectDto(id, name)
    }

    private fun getCsvLines(): List<String> {
        return listOf(
            "id,name",
            "045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f,The chance",
            "045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f,Work",
            "045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f,Homework",
        )
    }
}