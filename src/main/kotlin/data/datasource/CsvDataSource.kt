package data.datasource

import logic.models.*
import utilities.csv_parser.*

class CsvDataSource(
    private val userCsvParser: UserCsvParser,
    private val projectCsvParser: ProjectCsvParser,
    private val taskCsvParser: TaskCsvParser,
    private val stateCsvParser: StateCsvParser,
    private val auditLogCsvParser: AuditLogCsvParser,
) : DataSource {
    override fun getUsers(): List<User> {
        return emptyList()
    }

    override fun getProjects(): List<Project> {
        return emptyList()
    }

    override fun getTasks(): List<Task> {
        return emptyList()
    }

    override fun getStates(): List<State> {
        return emptyList()
    }

    override fun getAuditLogs(): List<AuditLog> {
        return emptyList()
    }
}