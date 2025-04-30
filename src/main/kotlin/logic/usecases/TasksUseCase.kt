package logic.usecases


import data.repositories.TasksRepositoryImpl
import logic.models.Task
import logic.repositoies.TasksRepository


class TasksUseCase(
    private val tasksRepository: TasksRepository
) {
    fun addTask(task: Task): List<Task>{
        val allTasks= tasksRepository.getAllTasks().toMutableList()
        allTasks.add(task)
        return allTasks
    }

    fun editTask(task: Task): Task{
       val allTasks= tasksRepository.getAllTasks().toMutableList()
        val taskID = task.taskID?:throw Exception()
        val newTask = allTasks
            .find {  currentTask-> currentTask.taskID==taskID }
            ?:throw Exception()

        newTask.taskTitle = task.taskTitle
        newTask.state = task.state
        newTask.taskDescription=task.taskDescription

        return newTask
    }



    fun deleteTask(id: Int?): List<Task>{
       val allTasks = tasksRepository.getAllTasks().toMutableList()
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

    fun deleteAllTasks(isAdmin: Boolean ): Boolean{
        val allTasks = tasksRepository.getAllTasks().toMutableList()
        if (isAdmin){
            allTasks.clear()
            return true
        }
        return false
    }

    fun getTaskById(id: Int?): Task {
        val allTasks= tasksRepository.getAllTasks()
       return allTasks.find { it.taskID==id }?: throw Exception()
    }
}