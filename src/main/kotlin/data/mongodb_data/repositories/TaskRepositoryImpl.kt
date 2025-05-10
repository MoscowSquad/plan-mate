package data.mongodb_data.repositories


import data.data_source.AuditLogDataSource
import data.data_source.TaskDataSource
import data.mongodb_data.dto.AuditLogDto
import data.mongodb_data.mappers.toDto
import data.mongodb_data.mappers.toTask
import data.mongodb_data.util.executeInIO
import kotlinx.datetime.Clock
import logic.models.AuditType
import logic.models.Task
import logic.repositories.TasksRepository
import java.util.*


class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
    private val auditLogDataSource: AuditLogDataSource
) : TasksRepository {


    override fun getAllTasks() = executeInIO {
        taskDataSource.getAllTasks().map {
            it.toTask()
        }
    }

    override fun addTask(task: Task) = executeInIO {
        val result = taskDataSource.addTask(task.toDto())
        auditLogDataSource.addLog(
            log = AuditLogDto(
                id = UUID.randomUUID().toString(),
                action = "Task named ${task.name} with description ${task.description} and id ${task.id} Created",
                entityId = task.id.toString(),
                timestamp = Clock.System.now().toString(),
                auditType = AuditType.TASK.toString(),
            )
        )
        return@executeInIO result
    }


    override fun editTask(updatedTask: Task) = executeInIO {
        val result = taskDataSource.editTask(updatedTask.toDto())
        auditLogDataSource.addLog(
            log = AuditLogDto(
                id = UUID.randomUUID().toString(),
                action = "Task named ${updatedTask.name} with description ${updatedTask.description} and id ${updatedTask.id} Created",
                entityId = updatedTask.id.toString(),
                timestamp = Clock.System.now().toString(),
                auditType = AuditType.TASK.toString(),
            )
        )
        return@executeInIO result
    }


    override fun deleteTask(taskId: UUID) = executeInIO {
        val result = taskDataSource.deleteTask(taskId)
        auditLogDataSource.addLog(
            log = AuditLogDto(
                id = UUID.randomUUID().toString(),
                action = "Task with id $taskId Deleted",
                entityId = taskId.toString(),
                timestamp = Clock.System.now().toString(),
                auditType = AuditType.TASK.toString(),
            )
        )
        return@executeInIO result
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