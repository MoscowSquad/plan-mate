package logic.usecases.tasksUseCases

import logic.models.Task
import logic.repositoies.TasksRepository

class AddTaskUseCase(
    private val tasksRepository: TasksRepository
) {
    operator fun invoke(task: Task): Boolean {
        return tasksRepository.add(task)
    }
}