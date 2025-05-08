package data.mongodb_data.datasource

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.firstOrNull
import logic.models.TaskState
import org.bson.AbstractBsonReader.State
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
        TODO("Not yet implemented")
    }

    override suspend fun update(state: TaskState) {
        TODO("Not yet implemented")
    }

    override suspend fun add(projectId: UUID, state: TaskState) {
        val stateWithProject = state.copy(projectId = projectId)
        collection.insertOne(stateWithProject)
    }

    override suspend fun delete(projectId: UUID, stateId: UUID) {
        TODO("Not yet implemented")
    }


}