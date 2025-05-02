package logic.usecases.tasksUseCases

import logic.models.Task
import logic.repositoies.TasksRepository

class AddTaskUseCase(
    private val tasksRepository: TasksRepository
) {
    fun addTask(task: Task): Boolean {
        return tasksRepository.addTask(task)
    }
}