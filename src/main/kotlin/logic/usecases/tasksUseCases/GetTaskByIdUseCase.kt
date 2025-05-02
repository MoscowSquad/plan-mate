package logic.usecases.tasksUseCases

import logic.models.Task
import logic.repositoies.TasksRepository
import java.util.*

class GetTaskByIdUseCase(
    private val tasksRepository: TasksRepository
) {

    operator fun invoke(id: UUID): Task {
        return tasksRepository.getById(id)
    }
}