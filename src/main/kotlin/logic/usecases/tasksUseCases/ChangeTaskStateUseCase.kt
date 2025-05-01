package logic.usecases.tasksUseCases

import logic.models.Task
import logic.repositoies.TasksRepository
import utilities.PropertyNullException
import utilities.TaskIsNotFoundException

class ChangeTaskStateUseCase(
    private val tasksRepository: TasksRepository
) {

    fun changeTaskState(task: Task): Task {
        val allTasks = tasksRepository.getAll().toMutableList()
        val taskId = task.id ?: throw PropertyNullException()
        val taskToEdit = allTasks.find { currentTask ->
            currentTask.id == taskId
        } ?: throw TaskIsNotFoundException(task.id)


        taskToEdit.state = task.state

        return taskToEdit
    }
}