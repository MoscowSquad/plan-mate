package logic.usecases.tasksUseCases

import logic.models.Task
import logic.repositoies.TasksRepository
import utilities.TaskIsNotFoundException

class EditTaskUseCase(
    private val tasksRepository: TasksRepository
) {
    fun editTask(task: Task): Boolean {
        val allTasks = tasksRepository.getAll().toMutableList()
        val taskIndex: Int = allTasks.indexOfFirst { it.id == task.id }

        if (taskIndex == -1) {
            throw TaskIsNotFoundException(task.id)
        }

        allTasks[taskIndex] = task.copy(
            id = task.id,
            title = task.title,
            description = task.description,
            stateId = task.stateId
        )

        return true
    }
}