package data.repositories

import data.datasource.TaskDataSource
import logic.models.Task
import logic.repositories.TasksRepository
import utilities.TaskIsExist
import utilities.TaskIsNotFoundException
import java.util.*

class TasksRepositoryImpl(
    private val taskDataSource: TaskDataSource
) : TasksRepository {

    private val tasks = mutableListOf<Task>()

    init {
        tasks.addAll(taskDataSource.fetch())
    }

    override fun getAll(): List<Task> = tasks.toList()

    override fun add(task: Task): Boolean {
        if (tasks.any { it.id == task.id }) throw TaskIsExist(task.id)
        tasks.add(task)
        taskDataSource.save(tasks)
        return true
    }

    override fun edit(updatedTask: Task): Boolean {
        val index = tasks.indexOfFirst { it.id == updatedTask.id }
        if (index == -1) throw TaskIsNotFoundException(updatedTask.id)

        tasks[index] = updatedTask
        taskDataSource.save(tasks)
        return true
    }

    override fun delete(taskId: UUID): Boolean {
        val removed = tasks.removeIf { it.id == taskId }
        if (!removed) throw TaskIsNotFoundException(taskId)

        taskDataSource.save(tasks)
        return true
    }

    override fun getById(taskId: UUID): Task {
        return tasks.find { it.id == taskId } ?: throw TaskIsNotFoundException(taskId)
    }

    override fun getByProjectId(taskId: UUID): List<Task> {
        return tasks.filter { it.projectId == taskId }
    }
}
