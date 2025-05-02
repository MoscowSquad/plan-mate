package data.repositories

import logic.models.Task
import logic.repositoies.TasksRepository
import utilities.TaskIsNotFoundException
import java.util.*

class TasksRepositoryImpl(

) : TasksRepository {

    private val tasks = mutableListOf<Task>()

    override fun getAll(): List<Task> = tasks

    override fun addTask(task: Task): Boolean {
        return tasks.add(task)
        // duplicated task
    }

    override fun editTask(updatedTask: Task): Boolean {
        val index = tasks.indexOfFirst { it.id == updatedTask.id }
        return if (index != -1) {
            tasks[index] = updatedTask
            true
        } else
            false
    }

    override fun deleteTask(taskId: UUID): Boolean {
        return tasks.removeIf { it.id == taskId }
    }

    override fun getTaskById(taskId: UUID): Task {
        return tasks.find { it.id == taskId } ?: throw TaskIsNotFoundException(taskId)
    }

    override fun getTaskByProjectId(taskId: UUID): List<Task> {
        return tasks.filter { currentTask ->
            currentTask.projectId == taskId
        }
    }

}