package logic.usecases.tasksUseCases

import logic.models.Task
import logic.repositoies.TasksRepository

class EditTaskUseCase(
    private val tasksRepository: TasksRepository
) {
    fun editTask(task: Task): Boolean {
        return tasksRepository.editTask(task)
    }
}