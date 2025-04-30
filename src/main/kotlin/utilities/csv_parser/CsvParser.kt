package utilities.csv_parser

typealias CsvData = String

const val USERS_FILE = "users.csv"
const val PROJECTS_FILE = "projects.csv"
const val TASKS_FILE = "tasks.csv"
const val STATES_FILE = "states.csv"
const val AUDIT_LOG_FILE = "audit_log.csv"

interface CsvParser<T> {
    fun parse(): List<T>
    fun serialize(data: List<T>): CsvData
}