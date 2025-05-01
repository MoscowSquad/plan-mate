package logic.usecases.tasksUseCases

import logic.models.Task
import logic.repositoies.TasksRepository

class EditTaskUseCase(
    private val tasksRepository: TasksRepository
) {
    fun editTask(task: Task): Task {
        val allTasks = tasksRepository.getAll().toMutableList()
        val taskID = task.id
        val taskTitle = task.title ?: throw Exception()
        val newTask = allTasks
            .find { currentTask -> currentTask.id == taskID } ?: throw Exception()

        newTask.title = taskTitle
        newTask.state = task.state
        newTask.description = task.description

        return newTask
    }
}