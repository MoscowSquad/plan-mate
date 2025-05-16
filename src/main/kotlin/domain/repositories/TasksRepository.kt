package domain.repositories

import domain.models.Task
import java.util.*

interface TasksRepository {
    suspend fun getAllTasks(): List<Task>
    suspend fun addTask(task: Task): Boolean
    suspend fun editTask(updatedTask: Task): Boolean
    suspend fun deleteTask(taskId: UUID): Boolean
    suspend fun getTaskById(taskId: UUID): Task
    suspend fun getTaskByProjectId(taskId: UUID): List<Task>
}