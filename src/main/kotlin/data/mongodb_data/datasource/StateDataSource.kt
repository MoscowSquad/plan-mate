package data.mongodb_data.datasource


import data.mongodb_data.dto.TaskStateDto
import java.util.*

interface StateDataSource {
    suspend fun getById(id: UUID): TaskStateDto
    suspend fun getByProjectId(projectId: UUID): List<TaskStateDto>
    suspend fun update(state: TaskStateDto): Boolean
    suspend fun add(projectId: UUID, state: TaskStateDto): Boolean
    suspend fun delete(projectId: UUID, stateId: UUID): Boolean

}