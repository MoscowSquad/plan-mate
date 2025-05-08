package data.mongodb_data.datasource

import logic.models.TaskState
import java.util.*

interface StateDataSource {
    suspend fun getById(id: UUID): TaskState
    suspend fun getByProjectId(projectId: UUID): List<TaskState>
    suspend fun update(state: TaskState): Boolean
    suspend fun add(projectId: UUID, state: TaskState): Boolean
    suspend fun delete(projectId: UUID, stateId: UUID): Boolean

}