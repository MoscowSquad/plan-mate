package logic.usecases


import logic.models.Task
import logic.repositoies.TasksRepository


class TasksUseCase(
    private val tasksRepository: TasksRepository
) {
    fun addTask(task: Task): List<Task>{
        val allTasks= tasksRepository.getAllTasks().toMutableList()
        if (task.taskTitle == null || task.taskID == null || task.state == null) {
            throw Exception()
        }
        allTasks.add(task)
        return allTasks
    }

    fun editTask(task: Task): Task{
        val allTasks = tasksRepository.getAllTasks().toMutableList()
        val taskID = task.taskID?:throw Exception()
        val taskTitle = task.taskTitle ?: throw Exception()
        val newTask = allTasks
            .find { currentTask -> currentTask.taskID == taskID }
            ?:throw Exception()

        newTask.taskTitle = task.taskTitle
        newTask.state = task.state
        newTask.taskDescription = task.taskDescription

        return newTask
    }



    fun deleteTask(id: Int?): List<Task>{
        val allTasks = tasksRepository.getAllTasks().toMutableList()
        if (allTasks.isEmpty()) {
            throw Exception()
        }
        val taskID=id?:throw Exception()
        allTasks.removeIf { currentTask->
            currentTask.taskID == taskID
        }
        return allTasks
    }


    fun changeTaskState(task:Task): Task{
        val allTasks = tasksRepository.getAllTasks().toMutableList()
        val taskID = task.taskID?:throw Exception()
        val taskToEdit = allTasks.find { currentTask->
            currentTask.taskID == taskID
        }?: throw Exception()

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
        val allTasks= tasksRepository.getAllTasks()
        return allTasks.find { it.taskID == id } ?: throw Exception()
    }
}