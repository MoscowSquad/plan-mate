package data.mongodb_data.repositories

import data.data_source.AuditLogDataSource
import data.data_source.TaskStateDataSource
import data.mongodb_data.dto.AuditLogDto
import data.mongodb_data.mappers.toDto
import data.mongodb_data.mappers.toTaskState
import data.mongodb_data.util.ensureAdminPrivileges
import data.mongodb_data.util.executeInIO
import domain.models.AuditLog.AuditType
import domain.models.TaskState
import domain.repositories.TaskStateRepository
import kotlinx.datetime.Clock
import java.util.*

class TaskStateRepositoryImpl(
    private val taskStateDataSource: TaskStateDataSource,
    private val auditLogDataSource: AuditLogDataSource,
) : TaskStateRepository {

    override suspend fun getTaskStateById(id: UUID): TaskState =
        executeInIO { taskStateDataSource.getTaskStateById(id).toTaskState() }


    override suspend fun getTaskStateByProjectId(projectId: UUID): List<TaskState> = executeInIO {
        taskStateDataSource.getTaskStateByProjectId(projectId).map {
            it.toTaskState()
        }
    }

    override suspend fun updateTaskState(state: TaskState): Boolean = executeInIO {
        ensureAdminPrivileges()
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

    override suspend fun addTaskState(projectId: UUID, state: TaskState): Boolean = executeInIO {
        ensureAdminPrivileges()
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

    override suspend fun deleteTaskState(projectId: UUID, stateId: UUID): Boolean = executeInIO {
        ensureAdminPrivileges()
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