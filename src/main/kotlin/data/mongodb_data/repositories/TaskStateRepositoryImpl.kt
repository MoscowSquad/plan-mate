package data.mongodb_data.repositories

import data.data_source.AuditLogDataSource
import data.data_source.TaskStateDataSource
import data.mongodb_data.dto.AuditLogDto
import data.mongodb_data.mappers.toDto
import data.mongodb_data.mappers.toTaskState
import data.mongodb_data.util.executeInIO
import data.mongodb_data.util.executeInIOAdminOnly
import kotlinx.datetime.Clock
import logic.models.AuditLog.AuditType
import logic.models.TaskState
import logic.repositories.TaskStateRepository
import java.util.*

class TaskStateRepositoryImpl(
    private val taskStateDataSource: TaskStateDataSource,
    private val auditLogDataSource: AuditLogDataSource,
) : TaskStateRepository {

    override fun getTaskStateById(id: UUID): TaskState =
        executeInIO { taskStateDataSource.getTaskStateById(id).toTaskState() }


    override fun getTaskStateByProjectId(projectId: UUID): List<TaskState> = executeInIO {
        taskStateDataSource.getTaskStateByProjectId(projectId).map {
            it.toTaskState()
        }
    }

    override fun updateTaskState(state: TaskState): Boolean = executeInIOAdminOnly {
        val result = taskStateDataSource.updateTaskState(state.toDto())
        auditLogDataSource.addLog(
            log = AuditLogDto(
                id = UUID.randomUUID().toString(),
                action = "State with id ${state.id} in project id ${state.projectId} is Updated",
                entityId = state.id.toString(),
                timestamp = Clock.System.now().toString(),
                auditType = AuditType.TASK_STATE.toString(),
            )
        )
        result
    }

    override fun addTaskState(projectId: UUID, state: TaskState): Boolean = executeInIOAdminOnly {
        val result = taskStateDataSource.addTaskState(projectId, state.toDto())
        auditLogDataSource.addLog(
            log = AuditLogDto(
                id = UUID.randomUUID().toString(),
                action = "New State with id ${state.id} in project id $projectId is Added",
                entityId = state.id.toString(),
                timestamp = Clock.System.now().toString(),
                auditType = AuditType.TASK_STATE.toString(),
            )
        )
        result
    }

    override fun deleteTaskState(projectId: UUID, stateId: UUID): Boolean = executeInIOAdminOnly {
        val result = taskStateDataSource.deleteTaskState(projectId, stateId)
        auditLogDataSource.addLog(
            log = AuditLogDto(
                id = UUID.randomUUID().toString(),
                action = "State with id $stateId in project id $projectId is Deleted",
                entityId = stateId.toString(),
                timestamp = Clock.System.now().toString(),
                auditType = AuditType.TASK_STATE.toString(),
            )
        )
        result
    }

}