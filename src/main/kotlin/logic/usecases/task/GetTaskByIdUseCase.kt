package logic.usecases.task

import logic.models.Task
import logic.repositories.TasksRepository
import java.util.*

class GetTaskByIdUseCase(
    private val tasksRepository: TasksRepository
) {

    operator fun invoke(id: UUID): Task {
        return tasksRepository.getById(id)
    }
}