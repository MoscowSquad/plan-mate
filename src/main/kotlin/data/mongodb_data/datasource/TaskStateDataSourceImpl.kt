package data.mongodb_data.datasource

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import logic.models.TaskState
import logic.util.NoStateExistException
import java.util.*

class TaskStateDataSourceImpl(
    private val collection: MongoCollection<TaskState>

):TaskStateDataSource {
    override suspend fun getTaskStateById(id: UUID): TaskState {
        val filter = Filters.eq("id", id.toString())
        return collection.find(filter).firstOrNull()
            ?: throw NoStateExistException("Task state not found")
    }

    override suspend fun getTaskStateByProjectId(projectId: UUID): List<TaskState> {
        val filter = Filters.eq("entityId", projectId.toString())
        return collection.find(filter).toList()
    }

    override suspend fun updateTaskState(state: TaskState): Boolean{
        val filter = Filters.eq("id", state.id)
        return collection.replaceOne(filter, state).modifiedCount>0
    }

    override suspend fun addTaskState(projectId: UUID, state: TaskState): Boolean{
        val stateWithProject = state.copy(projectId = projectId)
       return collection.insertOne(stateWithProject).wasAcknowledged()
    }

    override suspend fun deleteTaskState(projectId: UUID, stateId: UUID): Boolean{
        val filter = Filters.eq("id", projectId.toString())
        return collection.deleteOne(filter).deletedCount>0

    }

}