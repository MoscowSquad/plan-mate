package data.csv_data.repositories

import data.csv_data.datasource.TaskDataSource
import data.csv_data.mappers.toDto
import data.csv_data.mappers.toTask
import domain.models.Task
import domain.repositories.TasksRepository
import domain.util.TaskIsExist
import domain.util.TaskIsNotFoundException
import java.util.*

class TasksRepositoryImpl(
    private val taskDataSource: TaskDataSource
) : TasksRepository {

    val tasks = mutableListOf<Task>()

    init {
        tasks.addAll(taskDataSource.fetch().map { it.toTask() })
    }

    override suspend fun getAllTasks(): List<Task> = tasks.toList()

    override suspend fun addTask(task: Task): Boolean {
        if (tasks.any { it.id == task.id }) throw TaskIsExist(task.id)
        tasks.add(task)
        taskDataSource.save(tasks.map { it.toDto() })
        return true
    }

    override suspend fun editTask(updatedTask: Task): Boolean {
        val index = tasks.indexOfFirst { it.id == updatedTask.id }
        if (index == -1) throw TaskIsNotFoundException(updatedTask.id)

        tasks[index] = updatedTask
        taskDataSource.save(tasks.map { it.toDto() })
        return true
    }

    override suspend fun deleteTask(taskId: UUID): Boolean {
        val removed = tasks.removeIf { it.id == taskId }
        if (!removed) throw TaskIsNotFoundException(taskId)

        taskDataSource.save(tasks.map { it.toDto() })
        return true
    }

    override suspend fun getTaskById(taskId: UUID): Task {
        return tasks.find { it.id == taskId } ?: throw TaskIsNotFoundException(taskId)
    }

    override suspend fun getTaskByProjectId(taskId: UUID): List<Task> {
        return tasks.filter { it.projectId == taskId }
    }
}
