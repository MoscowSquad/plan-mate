package logic.usecases.tasksUseCases

import logic.models.Task
import logic.repositoies.TasksRepository

class AddTaskUseCase(
    private val tasksRepository: TasksRepository
) {
    fun addTask(task: Task): List<Task> {
        val allTasks = tasksRepository.getAll().toMutableList()
        if (task.title == null || task.id == null || task.state == null) {
            throw Exception()
        }
        allTasks.add(task)
        return allTasks
    }

}