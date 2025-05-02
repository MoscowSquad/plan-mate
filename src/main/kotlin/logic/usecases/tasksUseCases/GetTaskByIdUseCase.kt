package logic.usecases.tasksUseCases

import logic.models.Task
import logic.repositoies.TasksRepository
import java.util.*

class GetTaskByIdUseCase(
    private val tasksRepository: TasksRepository
) {

    fun getTaskById(id: UUID): Task {
        return tasksRepository.getTaskById(id)
    }
}