package data.data_source

import data.mongodb_data.dto.TaskDto
import java.util.*

interface TaskDataSource {
    suspend fun getAllTasks(): List<TaskDto>
    suspend fun addTask(task: TaskDto): Boolean
    suspend fun editTask(updatedTask: TaskDto): Boolean
    suspend fun deleteTask(taskId: UUID): Boolean
    suspend fun getTaskById(taskId: UUID): TaskDto
    suspend fun getTaskByProjectId(projectId: UUID): List<TaskDto>

}