package logic.usecases.tasksUseCases

import logic.repositoies.TasksRepository
import utilities.PropertyNullException
import utilities.TaskIsNotFoundException
import java.util.*

class DeleteTaskUseCase(
    private val tasksRepository: TasksRepository
) {

    fun deleteTask(id: UUID?): Boolean {
        val allTasks = tasksRepository.getAll().toMutableList()
        val taskId = id?:throw  PropertyNullException()
        if (allTasks.isEmpty()) {
            throw TaskIsNotFoundException(taskId)
        }
        val isRemoved = allTasks.removeIf { currentTask ->
            currentTask.id == id
        }
        if (!isRemoved) {
            throw TaskIsNotFoundException(taskId)
        }
        return true
    }

}