package logic.usecases.task

import logic.models.Task
import logic.repositories.TasksRepository
import java.util.*

class GetTaskByProjectIdUseCase(
    private val tasksRepository: TasksRepository
) {
    operator fun invoke(id: UUID): List<Task> {
        return tasksRepository.getTaskByProjectId(id)
    }
}