package logic.usecases.tasksUseCases

import logic.models.Task
import logic.repositoies.TasksRepository
import utilities.PropertyNullException
import utilities.TaskIsNotFoundException

class GetTaskByIdUseCase(
    private val tasksRepository: TasksRepository
) {

    fun getTaskById(id: Int?): Task {
        val allTasks = tasksRepository.getAll()
        val taskID = id?: throw PropertyNullException()

        return allTasks.find { it.id == id } ?: throw TaskIsNotFoundException(taskID)
    }
}