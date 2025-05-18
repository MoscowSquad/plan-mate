package data.datasource

import com.google.common.truth.Truth
import data.csv_data.csv_parser.CsvHandler
import data.csv_data.csv_parser.TaskCsvParser
import data.csv_data.datasource.TaskDataSource
import data.csv_data.dto.TaskDto
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
        // Given
        val csvLines = listOf("id,name,description,projectId,stateId", "1,Task1,Description,proj1,state1")
        every { csvHandler.getLines() } returns csvLines

        // When
        dataSource.fetch()

        // Then
        verify { taskCsvParser.parse(csvLines) }
    }

    @Test
    fun `fetch() should return parsed tasks when there is tasks returned by task csv-parser`() {
        // Given
        val projectId = UUID.randomUUID().toString()
        val stateId = UUID.randomUUID().toString()
        val description = "Watch all videos"
        val tasks = listOf(
            TaskDto(UUID.randomUUID().toString(), "Videos 101 to 110", description, projectId, stateId, listOf()),
            TaskDto(UUID.randomUUID().toString(), "Videos 201 to 210", description, projectId, stateId, listOf()),
            TaskDto(UUID.randomUUID().toString(), "Videos 301 to 210", description, projectId, stateId, listOf()),
            TaskDto(UUID.randomUUID().toString(), "Videos 401 to 210", description, projectId, stateId, listOf()),
        )
        every { csvHandler.getLines() } returns listOf("id,name,description,projectId,stateId")
        every { taskCsvParser.parse(any()) } returns tasks

        // When
        val result = dataSource.fetch()

        // Then
        Truth.assertThat(result).isEqualTo(tasks)
    }

    @Test
    fun `fetch() should return empty list when there is no tasks returned by task csv-parser`() {
        // Given
        val tasks = listOf<TaskDto>()
        every { csvHandler.getLines() } returns listOf("id,name,description,projectId,stateId")
        every { taskCsvParser.parse(any()) } returns tasks

        // When
        val result = dataSource.fetch()

        // Then
        Truth.assertThat(result).isEqualTo(tasks)
    }

    @Test
    fun `save() should call CsvParser to serialize and CsvHandler to write when saving task data`() {
        // Given
        val projectId = UUID.randomUUID().toString()
        val stateId = UUID.randomUUID().toString()
        val tasks = listOf(TaskDto("1", "Task1", "Description", projectId, stateId, listOf()))
        val serializedData = listOf("id,name,description,projectId,stateId", "1,Task1,Description,$projectId,$stateId")
        every { taskCsvParser.serialize(tasks) } returns serializedData

        // When
        dataSource.save(tasks)

        // Then
        verify { taskCsvParser.serialize(tasks) }
        verify { csvHandler.write(serializedData) }
    }
}