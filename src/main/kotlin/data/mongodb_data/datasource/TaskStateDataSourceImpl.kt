package data.mongodb_data.datasource

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.data_source.TaskStateDataSource
import data.mongodb_data.dto.TaskStateDto
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import logic.util.NoStateExistException
import java.util.*

class TaskStateDataSourceImpl(
    private val collection: MongoCollection<TaskStateDto>

) : TaskStateDataSource {
    override suspend fun getTaskStateById(id: UUID): TaskStateDto {
        val filter = Filters.eq(TaskStateDto::id.name, id.toString())
        return collection.find(filter).firstOrNull()
            ?: throw NoStateExistException("Task state not found")
    }


    override suspend fun getTaskStateByProjectId(projectId: UUID): List<TaskStateDto> {
        val filter = Filters.eq(TaskStateDto::projectId.name, projectId.toString())
        return collection.find(filter).toList()
    }

    override suspend fun updateTaskState(state: TaskStateDto): Boolean {
        val filter = Filters.eq(TaskStateDto::id.name, state.id)
        val updates = Updates.combine(
            Updates.set(TaskStateDto::id.name, state.id),
            Updates.set(TaskStateDto::name.name, state.name),
            Updates.set(TaskStateDto::projectId.name, state.projectId)
        )
        return collection.updateOne(filter, updates).wasAcknowledged()
    }

    override suspend fun addTaskState(projectId: UUID, state: TaskStateDto): Boolean {
        val stateWithProject = state.copy(projectId = projectId.toString())
        return collection.insertOne(stateWithProject).wasAcknowledged()
    }

    override suspend fun deleteTaskState(projectId: UUID, stateId: UUID): Boolean {
        val filter = Filters.and(
            Filters.eq(TaskStateDto::id.name, stateId.toString()),
            Filters.eq(TaskStateDto::projectId.name, projectId.toString())
        )
        val result = collection.deleteOne(filter)
        return result.deletedCount > 0
    }

}