package logic.repositoies

import logic.models.Task
import java.util.*

interface TasksRepository {
    fun getAll(): List<Task>
    fun addTask(task: Task): Boolean
    fun editTask(updatedTask: Task): Boolean
    fun deleteTask(taskId: UUID): Boolean
    fun getTaskById(taskId: UUID): Task
    fun getTaskByProjectId(taskId: UUID): List<Task>
}