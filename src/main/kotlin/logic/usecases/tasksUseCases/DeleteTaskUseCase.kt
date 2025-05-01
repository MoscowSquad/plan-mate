package logic.usecases.tasksUseCases

import logic.models.Task
import logic.repositoies.TasksRepository

class DeleteTaskUseCase(
    private val tasksRepository: TasksRepository
) {

    fun deleteTask(id: Int?): List<Task> {
        val allTasks = tasksRepository.getAll().toMutableList()
        if (allTasks.isEmpty()) {
            throw Exception()
        }
        val isRemoved = allTasks.removeIf { currentTask ->
            currentTask.id == id
        }
        if (!isRemoved) {
            throw Exception()
        }
        return allTasks
    }

}