package logic.usecases.task

import logic.models.Task
import logic.repositories.TasksRepository

class AddTaskUseCase(
    private val tasksRepository: TasksRepository
) {
    operator fun invoke(task: Task): Boolean {
        return tasksRepository.add(task)
    }
}