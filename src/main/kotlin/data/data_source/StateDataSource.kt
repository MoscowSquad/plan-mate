package data.data_source

import data.mongodb_data.dto.TaskStateDto
import java.util.UUID

interface StateDataSource {
    suspend fun getById(id: UUID): TaskStateDto
    suspend fun getByProjectId(projectId: UUID): List<TaskStateDto>
    suspend fun update(state: TaskStateDto): Boolean
    suspend fun add(projectId: UUID, state: TaskStateDto): Boolean
    suspend fun delete(projectId: UUID, stateId: UUID): Boolean

}