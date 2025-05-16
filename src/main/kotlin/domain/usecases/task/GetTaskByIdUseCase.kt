package domain.usecases.task

import domain.models.Task
import domain.repositories.TasksRepository
import java.util.*

class GetTaskByIdUseCase(
    private val tasksRepository: TasksRepository
) {
    suspend operator fun invoke(id: UUID): Task {
        return tasksRepository.getTaskById(id)
    }
}