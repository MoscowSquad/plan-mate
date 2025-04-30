package utilities.csv_parser

import java.io.File

abstract class CsvHandler(val file: File) {
    fun getLines(): List<String> {
        return emptyList()
    }

    fun write(lines: List<String>) {
    }
}

class UserCsvHandler(fileGetter: FileGetter) : CsvHandler(fileGetter.getFile(USERS_FILE))
class ProjectCsvHandler(fileGetter: FileGetter) : CsvHandler(fileGetter.getFile(PROJECTS_FILE))
class TaskCsvHandler(fileGetter: FileGetter) : CsvHandler(fileGetter.getFile(TASKS_FILE))
class StateCsvHandler(fileGetter: FileGetter) : CsvHandler(fileGetter.getFile(STATES_FILE))
class AuditLogCsvHandler(fileGetter: FileGetter) : CsvHandler(fileGetter.getFile(AUDIT_LOG_FILE))