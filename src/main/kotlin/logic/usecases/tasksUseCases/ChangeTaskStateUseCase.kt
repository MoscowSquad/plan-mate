package logic.usecases.tasksUseCases

import logic.models.Task
import logic.repositoies.TasksRepository

class ChangeTaskStateUseCase(
    private val tasksRepository: TasksRepository
) {

    fun changeTaskState(task: Task): Task {
        val allTasks = tasksRepository.getAll().toMutableList()
        val taskId = task.id ?: throw Exception()
        val taskToEdit = allTasks.find { currentTask ->
            currentTask.id == taskId
        } ?: throw Exception()


        taskToEdit.state = task.state

        return taskToEdit
    }
}