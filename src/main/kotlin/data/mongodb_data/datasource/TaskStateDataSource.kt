package data.mongodb_data.datasource

import logic.models.TaskState
import java.util.*

interface TaskStateDataSource {
    suspend fun getTaskStateById(id: UUID): TaskState
    suspend fun getTaskStateByProjectId(projectId: UUID): List<TaskState>
    suspend fun updateTaskState(state: TaskState)
    suspend fun addTaskState(projectId: UUID, state: TaskState)
    suspend fun deleteTaskState(projectId: UUID, stateId: UUID)

}