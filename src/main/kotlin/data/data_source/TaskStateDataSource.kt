package data.data_source

import data.mongodb_data.dto.TaskStateDto
import java.util.UUID

interface TaskStateDataSource {
    suspend fun getTaskStateById(id: UUID): TaskStateDto
    suspend fun getTaskStateByProjectId(projectId: UUID): List<TaskStateDto>
    suspend fun updateTaskState(state: TaskStateDto): Boolean
    suspend fun addTaskState(projectId: UUID, state: TaskStateDto): Boolean
    suspend fun deleteTaskState(projectId: UUID, stateId: UUID): Boolean

}