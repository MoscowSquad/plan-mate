package data.mongodb_data.datasource

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.data_source.TaskDataSource
import data.mongodb_data.dto.TaskDto
import data.mongodb_data.dto.TaskStateDto
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
        val filter = Filters.eq(TaskStateDto::id.name, updatedTask.id)
        val updates = Updates.combine(
            Updates.set(TaskDto::id.name, updatedTask.id),
            Updates.set(TaskDto::name.name, updatedTask.name),
            Updates.set(TaskDto::description.name, updatedTask.description),
            Updates.set(TaskDto::stateId.name, updatedTask.stateId),
            Updates.set(TaskDto::projectId.name, updatedTask.projectId),
            Updates.set(TaskDto::subTasks.name, updatedTask.subTasks)
        )
        return collection.updateOne(filter, updates).wasAcknowledged()
    }

    override suspend fun deleteTask(taskId: UUID): Boolean {
        val filter = Filters.eq(TaskStateDto::id.name, taskId.toString())
        return collection.deleteOne(filter).wasAcknowledged()
    }

    override suspend fun getTaskById(taskId: UUID): TaskDto {
        val filter = Filters.eq(TaskStateDto::id.name, taskId.toString())
        return collection.find(filter).firstOrNull()
            ?: throw NoSuchElementException("Task not found")
    }

    override suspend fun getTaskByProjectId(projectId: UUID): List<TaskDto> {
        val filter = Filters.eq(TaskStateDto::projectId.name, projectId.toString())
        return collection.find(filter).toList()
    }
}