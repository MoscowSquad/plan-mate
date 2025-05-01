package logic.usecases.tasksUseCases

import logic.models.Task
import logic.repositoies.TasksRepository

class GetTaskByIdUseCase(
    private val tasksRepository: TasksRepository
) {

    fun getTaskById(id: Int?): Task {
        val allTasks = tasksRepository.getAll()
        return allTasks.find { it.id == id } ?: throw Exception()
    }
}