package logic.usecases.tasksUseCases

import logic.models.Task
import logic.repositoies.TasksRepository
import java.util.*

class GetTaskByProjectIdUseCase(
    private val tasksRepository: TasksRepository
) {
    fun getTaskByProjectId(id: UUID): List<Task> {
        return tasksRepository.getTaskByProjectId(id)
    }
}