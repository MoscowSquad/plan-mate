package data.datasource

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import data.dto.TaskDto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import data.csv_parser.CsvHandler
import data.csv_parser.TaskCsvParser
import java.util.*

class TaskDataSourceTest {
    private lateinit var csvHandler: CsvHandler
    private lateinit var taskCsvParser: TaskCsvParser

    private lateinit var dataSource: TaskDataSource

    @BeforeEach
    fun setUp() {
        csvHandler = mockk(relaxed = true)
        taskCsvParser = mockk(relaxed = true)

        dataSource = TaskDataSource(csvHandler, taskCsvParser)
    }

    @Test
    fun `fetch() should call TaskCsvParser to parse when return tasks data`() {
        dataSource.fetch()
        verify { taskCsvParser.parse(any()) }
    }

    @Test
    fun `fetch() should return parsed tasks when there is tasks returned by task csv-parser`() {
        val projectId = UUID.randomUUID().toString()
        val stateId = UUID.randomUUID().toString()
        val description = "Watch all videos"
        val tasks = listOf(
            TaskDto(UUID.randomUUID().toString(), "Videos 101 to 110", description, projectId, stateId),
            TaskDto(UUID.randomUUID().toString(), "Videos 201 to 210", description, projectId, stateId),
            TaskDto(UUID.randomUUID().toString(), "Videos 301 to 210", description, projectId, stateId),
            TaskDto(UUID.randomUUID().toString(), "Videos 401 to 210", description, projectId, stateId),
        )
        every { taskCsvParser.parse(any()) } returns tasks

        val result = dataSource.fetch()
        Truth.assertThat(result).isEqualTo(tasks)
    }

    @Test
    fun `fetch() should return empty list when there is no tasks returned by task csv-parser`() {
        val tasks = listOf<TaskDto>()
        every { taskCsvParser.parse(any()) } returns tasks

        val result = dataSource.fetch()
        Truth.assertThat(result).isEqualTo(tasks)
    }

    @Test
    fun `save() should call CsvHandler to parse when save tasks data`() {
        dataSource.save(emptyList())
        verify { csvHandler.write(any()) }
    }
}