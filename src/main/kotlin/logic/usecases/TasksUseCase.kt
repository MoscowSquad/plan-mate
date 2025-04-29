package logic.usecases

import logic.models.Task
import logic.repositoies.TasksRepository

class TasksUseCase(
    private val tasksRepository: TasksRepository

) {
    fun addTask(): Boolean = false
    fun editTasks(): Boolean = false
    fun deleteTask(): Boolean = false
    fun deleteAllTasks(): Boolean = false
    fun changeTaskState(): Boolean = false
    fun getTaskById(): Boolean = false
}