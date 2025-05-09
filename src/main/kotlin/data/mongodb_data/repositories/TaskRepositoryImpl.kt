package data.mongodb_data.repositories


import data.data_source.TaskDataSource
import data.mongodb_data.mappers.toDto
import data.mongodb_data.mappers.toTask
import data.mongodb_data.util.executeInIO
import kotlinx.coroutines.runBlocking
import logic.models.Task
import logic.repositories.TasksRepository
import java.util.*


class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource
) : TasksRepository {


    override fun getAllTasks() = executeInIO {
        taskDataSource.getAllTasks().map {
            it.toTask()
        }
    }

    override fun addTask(task: Task): Boolean {
        return executeInIO {
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
        return executeInIO {
            try {
                taskDataSource.deleteTask(taskId)
                true
            } catch (e: Exception) {
                false
            }
        }
    }


    override fun getTaskById(taskId: UUID) = executeInIO {
        taskDataSource.getTaskById(taskId).toTask()
    }


    override fun getTaskByProjectId(taskId: UUID) = executeInIO {
        taskDataSource.getTaskByProjectId(taskId).map {
            it.toTask()
        }
    }


}