package data.mongodb_data.datasource


import data.mongodb_data.dto.TaskStateDto
import java.util.*

interface TaskStateDataSource {
    suspend fun getTaskStateById(id: UUID): TaskStateDto
    suspend fun getTaskStateByProjectId(projectId: UUID): List<TaskStateDto>
    suspend fun updateTaskState(state: TaskStateDto): Boolean
    suspend fun addTaskState(projectId: UUID, state: TaskStateDto): Boolean
    suspend fun deleteTaskState(projectId: UUID, stateId: UUID): Boolean

}