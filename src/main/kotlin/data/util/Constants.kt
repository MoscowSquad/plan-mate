package data.util

const val ADMIN = "ADMIN"
const val MATE = "MATE"

const val PROJECT = "PROJECT"
const val TASK = "TASK"

const val USERS_FILE = "users.csv"
const val PROJECTS_FILE = "projects.csv"
const val TASKS_FILE = "tasks.csv"
const val TASK_STATES_FILE = "states.csv"
const val AUDIT_LOG_FILE = "audit_log.csv"
const val TEST_FILE = "test.csv"

object UserIndex {
    const val ID: Int = 0
    const val NAME: Int = 1
    const val HASHED_PASSWORD: Int = 2
    const val ROLE: Int = 3
    const val PROJECT_IDS: Int = 4
}

object AuditLogIndex {
    const val ID: Int = 0
    const val ACTION: Int = 1
    const val AUDIT_TYPE: Int = 2
    const val TIMESTAMP: Int = 3
    const val ENTITY_ID: Int = 4
    const val USER_ID: Int = 5
}

object ProjectIndex {
    const val ID: Int = 0
    const val NAME: Int = 1
    const val USER_IDS: Int = 2
}

object TaskIndex {
    const val ID: Int = 0
    const val TITLE: Int = 1
    const val DESCRIPTION: Int = 2
    const val PROJECT_ID: Int = 3
    const val STATE_ID: Int = 4
}

object TaskStateIndex {
    const val ID: Int = 0
    const val TITLE: Int = 1
    const val PROJECT_ID: Int = 2
}
