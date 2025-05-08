package data.data_source

import data.mongodb_data.dto.TaskDto
import java.util.UUID

interface TaskDataSource {
    suspend fun getAllTasks(): List<TaskDto>
    suspend fun addTask(task: TaskDto)
    suspend fun editTask(updatedTask: TaskDto)
    suspend fun deleteTask(taskId: UUID)
    suspend fun getTaskById(taskId: UUID): TaskDto
    suspend fun getTaskByProjectId(projectId: UUID): List<TaskDto>

}