package data.mongodb_data.datasource

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import logic.models.TaskState
import java.util.*

class StateDataSourceImpl(
    private val collection: MongoCollection<TaskState>

):StateDataSource {
    override suspend fun getById(id: UUID): TaskState {
        val filter = Filters.eq("id", id.toString())
        return collection.find(filter).firstOrNull()
            ?: throw NoSuchElementException("state not found")
    }

    override suspend fun getByProjectId(projectId: UUID): List<TaskState> {
        val filter = Filters.eq("entityId", projectId.toString())
        return collection.find(filter).toList()
    }

    override suspend fun update(state: TaskState): Boolean {
        val filter = Filters.eq("id", state.id)
       val updateState= collection.replaceOne(filter, state)
        return updateState.modifiedCount>0
    }

    override suspend fun add(projectId: UUID, state: TaskState): Boolean {
        val stateWithProject = state.copy(projectId = projectId)
        val addedState = collection.insertOne(stateWithProject)
        return addedState.wasAcknowledged()
    }

    override suspend fun delete(projectId: UUID, stateId: UUID): Boolean {
        val filter = Filters.eq("id", projectId.toString())
        val deletedState = collection.deleteOne(filter)
        return deletedState.deletedCount>0
    }


}