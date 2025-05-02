package logic.usecases.task

import logic.models.Task
import logic.repositoies.TasksRepository

class EditTaskUseCase(
    private val tasksRepository: TasksRepository
) {
    operator fun invoke(task: Task): Boolean {
        return tasksRepository.edit(task)
    }
}