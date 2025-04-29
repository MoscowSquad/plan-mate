package logic.usecases


import logic.models.Task
import logic.repositoies.TasksRepository

class TasksUseCase(
    private val tasksRepository: TasksRepository
) {
    fun addTask(task: Task): Boolean = false
    fun editTasks(id: Int?): Boolean = false
    fun deleteTask(id: Int?): Boolean = false
    fun deleteAllTasks(): Boolean = false
    fun changeTaskState(): Boolean = false
    fun getTaskById(): Boolean = false
}