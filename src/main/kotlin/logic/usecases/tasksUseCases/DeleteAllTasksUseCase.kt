package logic.usecases.tasksUseCases

import logic.repositoies.TasksRepository

class DeleteAllTasksUseCase(
    private val tasksRepository: TasksRepository
) {

    fun deleteAllTasks(isAdmin: Boolean): Boolean {
        val allTasks = tasksRepository.getAll().toMutableList()
        if (isAdmin) {
            allTasks.clear()
            return true
        }
        return throw Exception()
    }
}