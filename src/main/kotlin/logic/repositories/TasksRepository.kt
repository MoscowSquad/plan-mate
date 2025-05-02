package logic.repositories

import logic.models.Task
import java.util.*

interface TasksRepository {
    fun getAll(): List<Task>
    fun add(task: Task): Boolean
    fun edit(updatedTask: Task): Boolean
    fun delete(taskId: UUID): Boolean
    fun getById(taskId: UUID): Task
    fun getByProjectId(taskId: UUID): List<Task>
}