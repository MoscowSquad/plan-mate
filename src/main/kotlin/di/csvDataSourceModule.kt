package di

import data.csv_data.csv_parser.*
import data.csv_data.datasource.*
import data.csv_data.util.*
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

private const val USERS_FILE = "src/main/resources/users.csv"
private const val PROJECTS_FILE = "src/main/resources/projects.csv"
private const val TASKS_FILE = "src/main/resources/tasks.csv"
private const val TASK_STATES_FILE = "src/main/resources/states.csv"
private const val AUDIT_LOG_FILE = "src/main/resources/audit_log.csv"

val csvDataSourceModule = module {
    single(named(USERS_FILE)) { File(USERS_FILE) }
    single(named(PROJECTS_FILE)) { File(PROJECTS_FILE) }
    single(named(TASKS_FILE)) { File(TASKS_FILE) }
    single(named(TASK_STATES_FILE)) { File(TASK_STATES_FILE) }
    single(named(AUDIT_LOG_FILE)) { File(AUDIT_LOG_FILE) }

    single(named(USERS_FILE)) { CsvHandler(get(named(USERS_FILE))) }
    single(named(PROJECTS_FILE)) { CsvHandler(get(named(PROJECTS_FILE))) }
    single(named(TASKS_FILE)) { CsvHandler(get(named(TASKS_FILE))) }
    single(named(TASK_STATES_FILE)) { CsvHandler(get(named(TASK_STATES_FILE))) }
    single(named(AUDIT_LOG_FILE)) { CsvHandler(get(named(AUDIT_LOG_FILE))) }

    singleOf(::UserCsvParser)
    singleOf(::ProjectCsvParser)
    singleOf(::TaskCsvParser)
    singleOf(::TaskStateCsvParser)
    singleOf(::AuditLogCsvParser)

    single { UserDataSource(get(named(USERS_FILE)), get()) }
    single { ProjectDataSource(get(named(PROJECTS_FILE)), get()) }
    single { TaskDataSource(get(named(TASKS_FILE)), get()) }
    single { TaskStateDataSource(get(named(TASK_STATES_FILE)), get()) }
    single { AuditLogDataSource(get(named(AUDIT_LOG_FILE)), get()) }
}