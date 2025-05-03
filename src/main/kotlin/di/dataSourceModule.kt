package di

import data.csv_parser.*
import data.datasource.*
import data.util.*
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val dataSourceModule = module {
    single(named(USERS_FILE)) { File(USERS_FILE) }
    single(named(PROJECTS_FILE)) { File(PROJECTS_FILE) }
    single(named(TASKS_FILE)) { File(TASKS_FILE) }
    single(named(TASK_STATES_FILE)) { File(TASK_STATES_FILE) }
    single(named(AUDIT_LOG_FILE)) { File(AUDIT_LOG_FILE) }

    single { CsvHandler(get(named(USERS_FILE))) }
    single { CsvHandler(get(named(PROJECTS_FILE))) }
    single { CsvHandler(get(named(TASKS_FILE))) }
    single { CsvHandler(get(named(TASK_STATES_FILE))) }
    single { CsvHandler(get(named(AUDIT_LOG_FILE))) }

    single { UserCsvParser() }
    single { ProjectCsvParser() }
    single { TaskCsvParser() }
    single { TaskStateCsvParser() }
    single { AuditLogCsvParser() }

    single { UserDataSource(get(), get()) }
    single { ProjectDataSource(get(), get()) }
    single { TaskDataSource(get(), get()) }
    single { TaskStateDataSource(get(), get()) }
    single { AuditLogDataSource(get(), get()) }
}