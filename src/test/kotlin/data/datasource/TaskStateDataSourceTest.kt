package data.datasource

import com.google.common.truth.Truth
import data.csv_parser.CsvHandler
import data.csv_parser.TaskStateCsvParser
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.State
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
        dataSource.fetch()
        verify { stateCsvParser.parse(any()) }
    }

    @Test
    fun `fetch() should return parsed states when there is states returned by state csv-parser`() {
        val projectId = UUID.randomUUID()
        val states = listOf(
            State(UUID.randomUUID(), "Todo", projectId),
            State(UUID.randomUUID(), "In progress", projectId),
            State(UUID.randomUUID(), "Done", projectId),
        )
        every { stateCsvParser.parse(any()) } returns states

        val result = dataSource.fetch()
        Truth.assertThat(result).isEqualTo(states)
    }

    @Test
    fun `fetch() should return empty list when there is no states returned by state csv-parser`() {
        val states = listOf<State>()
        every { stateCsvParser.parse(any()) } returns states

        val result = dataSource.fetch()
        Truth.assertThat(result).isEqualTo(states)
    }

    @Test
    fun `save() should call CsvHandler to parse when save tasks states data`() {
        dataSource.save(emptyList())
        verify { csvHandler.write(any(), any()) }
    }
}