package logic.usecases.tasksUseCases

import logic.models.Task
import logic.repositoies.TasksRepository
import java.util.*

class GetTaskByProjectIdUseCase(
    private val tasksRepository: TasksRepository
) {
    operator fun invoke(id: UUID): List<Task> {
        return tasksRepository.getByProjectId(id)
    }
}