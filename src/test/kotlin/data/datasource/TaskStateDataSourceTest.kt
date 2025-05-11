package data.datasource

import com.google.common.truth.Truth
import data.csv_data.csv_parser.CsvHandler
import data.csv_data.csv_parser.TaskStateCsvParser
import data.csv_data.datasource.TaskStateDataSource
import data.csv_data.dto.TaskStateDto
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class TaskStateDataSourceTest {
    private lateinit var csvHandler: CsvHandler
    private lateinit var stateCsvParser: TaskStateCsvParser

    private lateinit var dataSource: TaskStateDataSource

    @BeforeEach
    fun setUp() {
        csvHandler = mockk(relaxed = true)
        stateCsvParser = mockk(relaxed = true)
        dataSource = TaskStateDataSource(csvHandler, stateCsvParser)
    }

    @Test
    fun `fetch() should call StateCsvParser to parse when return states data`() {
        // Given
        val csvLines = listOf("id,name,projectId", "1,Todo,proj1")
        every { csvHandler.getLines() } returns csvLines

        // When
        dataSource.fetch()

        // Then
        verify { stateCsvParser.parse(csvLines) }
    }

    @Test
    fun `fetch() should return parsed states when there is states returned by state csv-parser`() {
        // Given
        val projectId = UUID.randomUUID().toString()
        val states = listOf(
            TaskStateDto(UUID.randomUUID().toString(), "Todo", projectId),
            TaskStateDto(UUID.randomUUID().toString(), "In progress", projectId),
            TaskStateDto(UUID.randomUUID().toString(), "Done", projectId),
        )
        every { csvHandler.getLines() } returns listOf("id,name,projectId")
        every { stateCsvParser.parse(any()) } returns states

        // When
        val result = dataSource.fetch()

        // Then
        Truth.assertThat(result).isEqualTo(states)
    }

    @Test
    fun `fetch() should return empty list when there is no states returned by state csv-parser`() {
        // Given
        val states = listOf<TaskStateDto>()
        every { csvHandler.getLines() } returns listOf("id,name,projectId")
        every { stateCsvParser.parse(any()) } returns states

        // When
        val result = dataSource.fetch()

        // Then
        Truth.assertThat(result).isEqualTo(states)
    }

    @Test
    fun `save() should call CsvParser to serialize and CsvHandler to write when saving state data`() {
        // Given
        val projectId = UUID.randomUUID().toString()
        val states = listOf(TaskStateDto("1", "Todo", projectId))
        val serializedData = listOf("id,name,projectId", "1,Todo,$projectId")
        every { stateCsvParser.serialize(states) } returns serializedData

        // When
        dataSource.save(states)

        // Then
        verify { stateCsvParser.serialize(states) }
        verify { csvHandler.write(serializedData) }
    }
}