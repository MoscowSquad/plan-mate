package logic.usecases


import logic.models.Task
import logic.repositoies.TasksRepository

class TasksUseCase(
    private val tasksRepository: TasksRepository
) {
    fun addTask(task: Task): Boolean = false
    fun editTasks(id: Int?): Boolean = false
    fun deleteTask(id: Int?): Boolean = false
    fun changeTaskState(id: Int?): Boolean = false
    fun deleteAllTasks(isAdmin: Boolean = false): Boolean = false
    fun getTaskById(id: Int?): Boolean = false
}