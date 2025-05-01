package logic.usecases.tasksUseCases

import logic.models.Task
import logic.repositoies.TasksRepository
import utilities.PropertyNullException
import utilities.TaskIsNotFoundException

class EditTaskUseCase(
    private val tasksRepository: TasksRepository
) {
    fun editTask(task: Task): Task {
        val allTasks = tasksRepository.getAll().toMutableList()
        val taskID = task.id?: throw PropertyNullException()
        val taskTitle = task.title ?: throw PropertyNullException()
        val newTask = allTasks
            .find { currentTask -> currentTask.id == taskID } ?: throw TaskIsNotFoundException(taskID)

        newTask.title = taskTitle
        newTask.state = task.state
        newTask.description = task.description

        return newTask
    }
}