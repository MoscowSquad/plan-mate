package data.mongodb_data.repositories


import data.data_source.TaskDataSource
import data.mongodb_data.mappers.toDto
import data.mongodb_data.mappers.toTask
import data.mongodb_data.util.executeInIO
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

    override fun addTask(task: Task) = executeInIO {
        taskDataSource.addTask(task.toDto())
    }


    override fun editTask(updatedTask: Task) = executeInIO {
        taskDataSource.editTask(updatedTask.toDto())
    }


    override fun deleteTask(taskId: UUID) = executeInIO {
        taskDataSource.deleteTask(taskId)
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