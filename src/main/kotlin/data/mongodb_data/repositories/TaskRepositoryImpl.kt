package data.mongodb_data.repositories


import data.data_source.TaskDataSource
import data.mongodb_data.mappers.toDto
import data.mongodb_data.mappers.toTask
import kotlinx.coroutines.*
import logic.models.Task
import logic.repositories.TasksRepository
import java.util.*


class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource
) : TasksRepository {
    private val scope = CoroutineScope(Dispatchers.IO)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllTasks(): List<Task> {
        val deferred = scope.async {
            taskDataSource.getAllTasks().map {
                it.toTask()
            }
        }
        return deferred.getCompleted()
    }

    override fun addTask(task: Task): Boolean {
        return runBlocking {
            try {
                taskDataSource.addTask(task.toDto())
                true
            } catch (e: Exception) {
                false
            }
        }
    }


    override fun editTask(updatedTask: Task): Boolean {
        return runBlocking {
            try {
                taskDataSource.editTask(updatedTask.toDto())
                true
            } catch (e: Exception) {
                false
            }
        }
    }


    override fun deleteTask(taskId: UUID): Boolean {
        return runBlocking {
            try {
                taskDataSource.deleteTask(taskId)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getTaskById(taskId: UUID): Task {
        val deferred = scope.async {
            taskDataSource.getTaskById(taskId).toTask()
        }
        return deferred.getCompleted()
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getTaskByProjectId(taskId: UUID): List<Task> {
        val deferred = scope.async {
            taskDataSource.getTaskByProjectId(taskId).map {
                it.toTask()
            }
        }
        return deferred.getCompleted()
    }


}