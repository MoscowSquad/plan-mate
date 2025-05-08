package data.mongodb_data.datasource

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.mongodb_data.dto.TaskDto
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import java.util.*


class TaskDataSourceImpl(
    private val collection: MongoCollection<TaskDto>
) : TaskDataSource {
    override suspend fun getAllTasks(): List<TaskDto> {
        return collection.find().toList()
    }

    override suspend fun addTask(task: TaskDto) {
        collection.insertOne(task)
    }

    override suspend fun editTask(updatedTask: TaskDto) {
        val filter = Filters.eq("id", updatedTask.id)
        collection.replaceOne(filter, updatedTask)
    }

    override suspend fun deleteTask(taskId: UUID) {
        val filter = Filters.eq("id", taskId.toString())
        collection.deleteOne(filter)
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