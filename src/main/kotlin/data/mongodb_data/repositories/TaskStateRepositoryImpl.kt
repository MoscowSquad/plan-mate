package data.mongodb_data.repositories

import data.data_source.TaskStateDataSource
import data.mongodb_data.mappers.toDto
import data.mongodb_data.mappers.toTaskState
import data.mongodb_data.util.executeInIO
import logic.models.TaskState
import logic.repositories.TaskStateRepository
import java.util.*

class TaskStateRepositoryImpl(
    private val taskStateDataSource: TaskStateDataSource
) : TaskStateRepository {

    override fun getTaskStateById(id: UUID): TaskState =
        executeInIO { taskStateDataSource.getTaskStateById(id).toTaskState() }


    override fun getTaskStateByProjectId(projectId: UUID): List<TaskState> = executeInIO {
        taskStateDataSource.getTaskStateByProjectId(projectId).map {
            it.toTaskState()
        }
    }

    override fun updateTaskState(state: TaskState): Boolean =
        executeInIO { taskStateDataSource.updateTaskState(state.toDto()) }

    override fun addTaskState(projectId: UUID, state: TaskState): Boolean =
        executeInIO { taskStateDataSource.addTaskState(projectId, state.toDto()) }

    override fun deleteTaskState(projectId: UUID, stateId: UUID): Boolean =
        executeInIO { taskStateDataSource.deleteTaskState(projectId, stateId) }

}