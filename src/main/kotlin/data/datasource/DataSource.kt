package data.datasource

import logic.models.*

interface DataSource {
    fun getUsers(): List<User>
    fun getProjects(): List<Project>
    fun getTasks(): List<Task>
    fun getStates(): List<State>
    fun getAuditLogs(): List<AuditLog>
}