package data.mongodb_data.datasource

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.data_source.TaskStateDataSource
import data.mongodb_data.dto.TaskStateDto
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import logic.util.NoStateExistException
import java.util.*

class TaskStateDataSourceImpl(
    private val collection: MongoCollection<TaskStateDto>

): TaskStateDataSource {
    override suspend fun getTaskStateById(id: UUID): TaskStateDto {
        val filter = Filters.eq("id", id.toString())
        return collection.find(filter).firstOrNull()
            ?: throw NoStateExistException("Task state not found")
    }

    override suspend fun getTaskStateByProjectId(projectId: UUID): List<TaskStateDto> {
        val filter = Filters.eq("entityId", projectId.toString())
        return collection.find(filter).toList()
    }

    override suspend fun updateTaskState(state: TaskStateDto): Boolean {
        val filter = Filters.eq("id", state.id)
        return collection.replaceOne(filter, state).wasAcknowledged()
    }

    override suspend fun addTaskState(projectId: UUID, state: TaskStateDto): Boolean {
        val stateWithProject = state.copy(projectId = projectId.toString())
       return collection.insertOne(stateWithProject).wasAcknowledged()
    }

    override suspend fun deleteTaskState(projectId: UUID, stateId: UUID): Boolean{
        val filter = Filters.eq("id", projectId.toString())
        return collection.deleteOne(filter).wasAcknowledged()
    }

}