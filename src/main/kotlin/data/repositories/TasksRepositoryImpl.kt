package data.repositories

import logic.models.Task
import logic.repositoies.TasksRepository
import utilities.TaskIsExist
import utilities.TaskIsNotFoundException
import java.util.*

class TasksRepositoryImpl(

) : TasksRepository {

    private val tasks = mutableListOf<Task>()

    override fun getAll(): List<Task> = tasks

    override fun add(task: Task): Boolean {
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index == -1) {
            tasks.add(task)
            return true
        } else {
            throw TaskIsExist(task.id)
        }
    }

    override fun edit(updatedTask: Task): Boolean {
        val index = tasks.indexOfFirst { it.id == updatedTask.id }
        return if (index != -1) {
            tasks[index] = updatedTask
            true
        } else
            throw TaskIsNotFoundException(updatedTask.id)

    }

    override fun delete(taskId: UUID): Boolean {
        val index = tasks.indexOfFirst { it.id == taskId }
        if (index == -1) {
            throw TaskIsNotFoundException(taskId)
        }
        tasks.removeIf { it.id == taskId }
        return true
    }

    override fun getById(taskId: UUID): Task {
        return tasks.find { it.id == taskId } ?: throw TaskIsNotFoundException(taskId)
    }

    override fun getByProjectId(taskId: UUID): List<Task> {
        val index = tasks.indexOfFirst { it.id == taskId }
        if (index == -1) {
            throw TaskIsNotFoundException(taskId)
        }
        return tasks.filter { currentTask ->
            currentTask.projectId == taskId
        }
    }

}