package logic.usecases


import logic.models.Task
import logic.repositoies.TasksRepository


class TasksUseCase(
    private val tasksRepository: TasksRepository
) {
    fun addTask(task: Task): List<Task> {
        val allTasks = tasksRepository.getAllTasks().toMutableList()
        if (task.title == null || task.id == null || task.state == null) {
            throw Exception()
        }
        allTasks.add(task)
        return allTasks
    }

    fun editTask(task: Task): Task {
        val allTasks = tasksRepository.getAllTasks().toMutableList()
        val taskID = task.id
        val taskTitle = task.title ?: throw Exception()
        val newTask = allTasks
            .find { currentTask -> currentTask.id == taskID } ?: throw Exception()

        newTask.title = taskTitle
        newTask.state = task.state
        newTask.description = task.description

        return newTask
    }


    fun deleteTask(id: Int?): List<Task> {
        val allTasks = tasksRepository.getAllTasks().toMutableList()
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


    fun changeTaskState(task: Task): Task {
        val allTasks = tasksRepository.getAllTasks().toMutableList()
        val taskId = task.id ?: throw Exception()
        val taskToEdit = allTasks.find { currentTask ->
            currentTask.id == taskId
        } ?: throw Exception()


        taskToEdit.state = task.state

        return taskToEdit
    }

    fun deleteAllTasks(isAdmin: Boolean): Boolean {
        val allTasks = tasksRepository.getAllTasks().toMutableList()
        if (isAdmin && allTasks.isNotEmpty()) {
            allTasks.clear()
            return true
        }
        return throw Exception()
    }

    fun getTaskById(id: Int?): Task {
        val allTasks = tasksRepository.getAllTasks()
        return allTasks.find { it.id == id } ?: throw Exception()
    }
}