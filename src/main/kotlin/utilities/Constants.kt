package utilities

const val USERS_FILE = "users.csv"
const val PROJECTS_FILE = "projects.csv"
const val TASKS_FILE = "tasks.csv"
const val TASK_STATES_FILE = "states.csv"
const val AUDIT_LOG_FILE = "audit_log.csv"
const val TEST_FILE = "test.csv"


object UserIndex {
    val ID: Int = 0
    val NAME: Int = 1
    val HASHED_PASSWORD: Int = 2
    val ROLE: Int = 3
    val PROJECT_IDS: Int = 4
}

object AuditLogIndex {
    val ID: Int = 0
    val ACTION: Int = 1
    val AUDITTYPE: Int = 2
    val TIMESTAMP: Int = 3
    val ENTITY_ID: Int = 4
    val USER_ID: Int = 5
}

object ProjectIndex {
    val ID: Int = 0
    val NAME: Int = 1
}

object TaskIndex {
    val ID: Int = 0
    val TITLE: Int = 1
    val DESCRIPTION: Int = 2
    val PROJECT_ID: Int = 3
    val STATE_ID: Int = 4
}

object TaskStateIndex {
    val ID: Int = 0
    val TITLE: Int = 1
    val PROJECT_ID: Int = 2
}
