package data.datasource

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import utilities.csv_parser.*
import java.time.LocalDateTime
import java.util.*

class CsvDataSourceTest {
    private lateinit var userCsvParser: UserCsvParser
    private lateinit var projectCsvParser: ProjectCsvParser
    private lateinit var taskCsvParser: TaskCsvParser
    private lateinit var stateCsvParser: StateCsvParser
    private lateinit var auditLogCsvParser: AuditLogCsvParser

    private lateinit var dataSource: CsvDataSource

    @BeforeEach
    fun setUp() {
        userCsvParser = mockk(relaxed = true)
        projectCsvParser = mockk(relaxed = true)
        taskCsvParser = mockk(relaxed = true)
        stateCsvParser = mockk(relaxed = true)
        auditLogCsvParser = mockk(relaxed = true)

        dataSource = CsvDataSource(
            userCsvParser,
            projectCsvParser,
            taskCsvParser,
            stateCsvParser,
            auditLogCsvParser,
        )
    }


    @Test
    fun `should call UserCsvParser to parse when return users data`() {
        dataSource.getUsers()
        verify { userCsvParser.parse() }
    }

    @Test
    fun `should return users when there is users returned by user csv-parser`() {
        val users = listOf(
            User(UUID.randomUUID(), "Aiman", "123456", Role.ADMIN),
            User(UUID.randomUUID(), "Muhammed", "123456", Role.MATE),
            User(UUID.randomUUID(), "Zeyad", "123456", Role.MATE),
            User(UUID.randomUUID(), "Yaser", "123456", Role.MATE),
        )
        every { userCsvParser.parse() } returns users

        val result = dataSource.getUsers()
        Truth.assertThat(result).isEqualTo(users)
    }

    @Test
    fun `should return empty list when there is no users returned by user csv-parser`() {
        val users = listOf<User>()
        every { userCsvParser.parse() } returns users

        val result = dataSource.getUsers()
        Truth.assertThat(result).isEqualTo(users)
    }

    @Test
    fun `should call ProjectCsvParser to parse when return projects data`() {
        dataSource.getUsers()
        verify { projectCsvParser.parse() }
    }

    @Test
    fun `should return projects when there is projects returned by project csv-parser`() {
        val projects = listOf(
            Project(UUID.randomUUID(), "The chance"),
            Project(UUID.randomUUID(), "Work"),
            Project(UUID.randomUUID(), "Study"),
        )
        every { projectCsvParser.parse() } returns projects

        val result = dataSource.getProjects()
        Truth.assertThat(result).isEqualTo(projects)
    }

    @Test
    fun `should return empty list when there is no projects returned by project csv-parser`() {
        val projects = listOf<Project>()
        every { projectCsvParser.parse() } returns projects

        val result = dataSource.getProjects()
        Truth.assertThat(result).isEqualTo(projects)
    }

    @Test
    fun `should call TaskCsvParser to parse when return tasks data`() {
        dataSource.getTasks()
        verify { taskCsvParser.parse() }
    }

    @Test
    fun `should return tasks when there is tasks returned by task csv-parser`() {
        val projectId = UUID.randomUUID()
        val stateId = UUID.randomUUID()
        val description = "Watch all videos"
        val tasks = listOf(
            Task(UUID.randomUUID(), "Videos 101 to 110", description, projectId, stateId),
            Task(UUID.randomUUID(), "Videos 201 to 210", description, projectId, stateId),
            Task(UUID.randomUUID(), "Videos 301 to 210", description, projectId, stateId),
            Task(UUID.randomUUID(), "Videos 401 to 210", description, projectId, stateId),
        )
        every { taskCsvParser.parse() } returns tasks

        val result = dataSource.getTasks()
        Truth.assertThat(result).isEqualTo(tasks)
    }

    @Test
    fun `should return empty list when there is no tasks returned by task csv-parser`() {
        val tasks = listOf<Task>()
        every { taskCsvParser.parse() } returns tasks

        val result = dataSource.getTasks()
        Truth.assertThat(result).isEqualTo(tasks)
    }

    @Test
    fun `should call StateCsvParser to parse when return states data`() {
        dataSource.getTasks()
        verify { stateCsvParser.parse() }
    }

    @Test
    fun `should return states when there is states returned by state csv-parser`() {
        val projectId = UUID.randomUUID()
        val states = listOf(
            State(UUID.randomUUID(), "Todo", projectId),
            State(UUID.randomUUID(), "In progress", projectId),
            State(UUID.randomUUID(), "Done", projectId),
        )
        every { stateCsvParser.parse() } returns states

        val result = dataSource.getStates()
        Truth.assertThat(result).isEqualTo(states)
    }

    @Test
    fun `should return empty list when there is no states returned by state csv-parser`() {
        val states = listOf<State>()
        every { stateCsvParser.parse() } returns states

        val result = dataSource.getStates()
        Truth.assertThat(result).isEqualTo(states)
    }

    @Test
    fun `should call AuditLogCsvParser to parse when return audit-logs data`() {
        dataSource.getTasks()
        verify { auditLogCsvParser.parse() }
    }

    @Test
    fun `should return audit-logs when there is audit-logs returned by audit-log csv-parser`() {
        val userId = UUID.randomUUID()
        val projectId = UUID.randomUUID()
        val taskId = UUID.randomUUID()
        val timestamp = LocalDateTime.now()
        val auditLogs = listOf(
            AuditLog(UUID.randomUUID(), EntityType.PROJECT, "Add", timestamp, projectId, userId),
            AuditLog(UUID.randomUUID(), EntityType.PROJECT, "Edit", timestamp, projectId, userId),
            AuditLog(UUID.randomUUID(), EntityType.PROJECT, "Delete", timestamp, projectId, userId),
            AuditLog(UUID.randomUUID(), EntityType.TASK, "Change State", timestamp, taskId, userId),
        )
        every { auditLogCsvParser.parse() } returns auditLogs

        val result = dataSource.getAuditLogs()
        Truth.assertThat(result).isEqualTo(auditLogs)
    }

    @Test
    fun `should return empty list when there is no audit-logs returned by audit-log csv-parser`() {
        val auditLogs = listOf<AuditLog>()
        every { auditLogCsvParser.parse() } returns auditLogs

        val result = dataSource.getAuditLogs()
        Truth.assertThat(result).isEqualTo(auditLogs)
    }
}