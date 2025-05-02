package logic.usecases.tasksUseCases

import logic.models.Task
import logic.repositoies.TasksRepository
import utilities.PropertyNullException
import utilities.TaskIsNotFoundException
import java.util.UUID

class DeleteTaskUseCase(
    private val tasksRepository: TasksRepository
) {

    fun deleteTask(id: UUID?): List<Task> {
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
        return allTasks
    }

}