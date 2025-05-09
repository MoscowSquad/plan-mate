package data.csv_data.repositories

import data.csv_data.datasource.TaskDataSource
import data.csv_data.mappers.toDto
import data.csv_data.mappers.toTask
import logic.models.Task
import logic.repositories.TasksRepository
import logic.util.TaskIsExist
import logic.util.TaskIsNotFoundException
import java.util.*

class TasksRepositoryImpl(
    private val taskDataSource: TaskDataSource
) : TasksRepository {

    private val tasks = mutableListOf<Task>()

    init {
        tasks.addAll(taskDataSource.fetch().map { it.toTask() })
    }

    override fun getAllTasks(): List<Task> = tasks.toList()

    override fun addTask(task: Task): Boolean {
        if (tasks.any { it.id == task.id }) throw TaskIsExist(task.id)
        tasks.add(task)
        taskDataSource.save(tasks.map { it.toDto() })
        return true
    }

    override fun editTask(updatedTask: Task): Boolean {
        val index = tasks.indexOfFirst { it.id == updatedTask.id }
        if (index == -1) throw TaskIsNotFoundException(updatedTask.id)

        tasks[index] = updatedTask
        taskDataSource.save(tasks.map { it.toDto() })
        return true
    }

    override fun deleteTask(taskId: UUID): Boolean {
        val removed = tasks.removeIf { it.id == taskId }
        if (!removed) throw TaskIsNotFoundException(taskId)

        taskDataSource.save(tasks.map { it.toDto() })
        return true
    }

    override fun getTaskById(taskId: UUID): Task {
        return tasks.find { it.id == taskId } ?: throw TaskIsNotFoundException(taskId)
    }

    override fun getTaskByProjectId(taskId: UUID): List<Task> {
        return tasks.filter { it.projectId == taskId }
    }
}
