package domain.usecases.task

import domain.models.Task
import domain.repositories.TasksRepository

class AddTaskUseCase(
    private val tasksRepository: TasksRepository
) {
    suspend operator fun invoke(task: Task): Boolean {
        return tasksRepository.addTask(task)
    }
}