package data.datasource

import com.google.common.truth.Truth
import data.csv_data.csv_parser.CsvHandler
import data.csv_data.csv_parser.ProjectCsvParser
import data.csv_data.datasource.ProjectDataSource
import data.csv_data.dto.ProjectDto
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class ProjectDataSourceTest {
    private lateinit var csvHandler: CsvHandler
    private lateinit var projectCsvParser: ProjectCsvParser
    private lateinit var dataSource: ProjectDataSource

    @BeforeEach
    fun setUp() {
        csvHandler = mockk(relaxed = true)
        projectCsvParser = mockk(relaxed = true)
        dataSource = ProjectDataSource(csvHandler, projectCsvParser)
    }

    @Test
    fun `fetch() should call ProjectCsvParser to parse when return projects data`() {
        // Given
        val csvLines = listOf("id,name", "1,Project1")
        every { csvHandler.getLines() } returns csvLines

        // When
        dataSource.fetch()

        // Then
        verify { projectCsvParser.parse(csvLines) }
    }

    @Test
    fun `fetch() should return parsed projects when there is projects returned by project csv-parser`() {
        // Given
        val projects = listOf(
            ProjectDto(UUID.randomUUID().toString(), "The chance"),
            ProjectDto(UUID.randomUUID().toString(), "Work"),
            ProjectDto(UUID.randomUUID().toString(), "Study"),
        )
        every { csvHandler.getLines() } returns listOf("id,name")
        every { projectCsvParser.parse(any()) } returns projects

        // When
        val result = dataSource.fetch()

        // Then
        Truth.assertThat(result).isEqualTo(projects)
    }

    @Test
    fun `fetch() should return empty list when there is no projects returned by project csv-parser`() {
        // Given
        val projects = listOf<ProjectDto>()
        every { csvHandler.getLines() } returns listOf("id,name")
        every { projectCsvParser.parse(any()) } returns projects

        // When
        val result = dataSource.fetch()

        // Then
        Truth.assertThat(result).isEqualTo(projects)
    }

    @Test
    fun `save() should call CsvParser to serialize and CsvHandler to write when saving project data`() {
        // Given
        val projects = listOf(ProjectDto("1", "Project1"))
        val serializedData = listOf("id,name", "1,Project1")
        every { projectCsvParser.serialize(projects) } returns serializedData

        // When
        dataSource.save(projects)

        // Then
        verify { projectCsvParser.serialize(projects) }
        verify { csvHandler.write(serializedData) }
    }
}