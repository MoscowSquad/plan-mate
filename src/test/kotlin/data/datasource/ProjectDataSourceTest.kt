package data.datasource

import com.google.common.truth.Truth
import data.csv_data.csv_parser.CsvHandler
import data.csv_data.csv_parser.ProjectCsvParser
import data.mongodb_data.datasource.ProjectDataSource
import data.mongodb_data.dto.ProjectDto
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
        dataSource.fetch()
        verify { projectCsvParser.parse(any()) }
    }

    @Test
    fun `fetch() should return parsed projects when there is projects returned by project csv-parser`() {
        val projects = listOf(
            ProjectDto(UUID.randomUUID().toString(), "The chance"),
            ProjectDto(UUID.randomUUID().toString(), "Work"),
            ProjectDto(UUID.randomUUID().toString(), "Study"),
        )
        every { projectCsvParser.parse(any()) } returns projects

        val result = dataSource.fetch()
        Truth.assertThat(result).isEqualTo(projects)
    }

    @Test
    fun `fetch() should return empty list when there is no projects returned by project csv-parser`() {
        val projects = listOf<ProjectDto>()
        every { projectCsvParser.parse(any()) } returns projects

        val result = dataSource.fetch()
        Truth.assertThat(result).isEqualTo(projects)
    }

    @Test
    fun `save() should call CsvHandler to parse when save projects data`() {
        dataSource.save(emptyList())
        verify { csvHandler.write(any()) }
    }
}