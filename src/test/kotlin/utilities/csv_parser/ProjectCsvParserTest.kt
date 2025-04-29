package utilities.csv_parser

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.Project
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import test_helper.toCsvData
import java.util.*

class ProjectCsvParserTest {
    private lateinit var csvReader: ProjectCsvReader
    private lateinit var csvWriter: ProjectCsvWriter
    private lateinit var parser: ProjectCsvParser

    @BeforeEach
    fun setUp() {
        csvReader = mockk(relaxed = true)
        csvWriter = mockk(relaxed = true)
        parser = ProjectCsvParser(csvReader, csvWriter)
    }

    @Test
    fun `should call ProjectCsvReader when parsing projects data`() {
        parser.parse()
        verify { csvReader.getLines() }
    }

    @Test
    fun `should call ProjectCsvWriter when serialize projects data`() {
        parser.serialize(emptyList())
        verify { csvWriter.write("") }
    }


    @Test
    fun `should return projects when parse data from project file`() {
        // Given
        val csvLines = getCsvLines()
        every { csvReader.getLines() } returns csvLines

        // When
        val result = parser.parse()

        // Then
        val projects = getProjects()
        Truth.assertThat(result).isEqualTo(projects)
    }

    @Test
    fun `should return empty list when parse data from empty project file`() {
        // Given
        val csvLines = listOf<String>()
        every { csvReader.getLines() } returns csvLines

        // When
        val result = parser.parse()

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
        every { csvReader.getLines() } returns csvLines

        // When
        val result = parser.parse()

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
            Project(UUID.fromString("82e16049-a9fb-4f69-b6f7-3336b68f2ae4"), "The chance"),
            Project(UUID.fromString("045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f"), "Work"),
            Project(UUID.fromString("07f641d4-077e-4f08-978d-3b6c9587f4bf"), "Homework"),
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