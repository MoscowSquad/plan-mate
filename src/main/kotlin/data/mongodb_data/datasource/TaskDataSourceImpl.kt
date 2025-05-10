package data.mongodb_data.datasource

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.data_source.TaskDataSource
import data.mongodb_data.dto.TaskDto
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import java.util.*


class TaskDataSourceImpl(
    private val collection: MongoCollection<TaskDto>,
) : TaskDataSource {
    override suspend fun getAllTasks(): List<TaskDto> {
        return collection.find().toList()
    }

    override suspend fun addTask(task: TaskDto): Boolean {

        return collection.insertOne(task).wasAcknowledged()
    }

    override suspend fun editTask(updatedTask: TaskDto): Boolean {
        val filter = Filters.eq("id", updatedTask.id)
        return collection.replaceOne(filter, updatedTask).wasAcknowledged()
    }

    override suspend fun deleteTask(taskId: UUID): Boolean {
        val filter = Filters.eq("id", taskId.toString())
        return collection.deleteOne(filter).wasAcknowledged()
    }

    override suspend fun getTaskById(taskId: UUID): TaskDto {
        val filter = Filters.eq("id", taskId.toString())
        return collection.find(filter).firstOrNull()
            ?: throw NoSuchElementException("Task not found")
    }

    override suspend fun getTaskByProjectId(projectId: UUID): List<TaskDto> {
        val filter = Filters.eq("projectId", projectId.toString())
        return collection.find(filter).toList()
    }
}