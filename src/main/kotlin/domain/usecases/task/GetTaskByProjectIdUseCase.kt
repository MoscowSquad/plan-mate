package domain.usecases.task

import domain.models.Task
import domain.repositories.TasksRepository
import java.util.*

class GetTaskByProjectIdUseCase(
    private val tasksRepository: TasksRepository
) {
    suspend operator fun invoke(id: UUID): List<Task> {
        return tasksRepository.getTaskByProjectId(id)
    }
}