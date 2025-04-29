package data.datasource

import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import utilities.csv_parser.*

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
}