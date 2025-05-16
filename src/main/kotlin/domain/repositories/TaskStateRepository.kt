package domain.repositories

import domain.models.TaskState
import java.util.*

interface TaskStateRepository {
    suspend fun getTaskStateById(id: UUID): TaskState
    suspend fun getTaskStateByProjectId(projectId: UUID): List<TaskState>
    suspend fun updateTaskState(state: TaskState): Boolean
    suspend fun addTaskState(projectId: UUID, state: TaskState): Boolean
    suspend fun deleteTaskState(projectId: UUID, stateId: UUID): Boolean
}