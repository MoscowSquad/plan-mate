package logic.usecases.tasksUseCases

import logic.models.Task
import logic.repositoies.TasksRepository
import utilities.PropertyNullException
import utilities.TaskIsNotFoundException
import java.util.UUID

class GetTaskByIdUseCase(
    private val tasksRepository: TasksRepository
) {

    fun getTaskById(id: UUID?): Task {
        val allTasks = tasksRepository.getAll()
        val taskID = id?: throw PropertyNullException()

        return allTasks.find { it.id == id } ?: throw TaskIsNotFoundException(taskID)
    }
}