package data.mongodb_data.repositories

import data.data_source.StateDataSource
import data.mongodb_data.mappers.toDto
import data.mongodb_data.mappers.toTaskState
import data.mongodb_data.util.executeInIO
import logic.models.TaskState
import logic.repositories.StateRepository
import java.util.*

class StateRepositoryImpl(
    private val stateDataSource: StateDataSource
):StateRepository {

    override fun getById(id: UUID) = executeInIO { stateDataSource.getById(id).toTaskState() }

    override fun getByProjectId(projectId: UUID) = executeInIO {
        stateDataSource.getByProjectId(projectId).map {
            it.toTaskState()
        }
    }

    override fun update(state: TaskState) = executeInIO {
        stateDataSource.update(state.toDto())
    }

    override fun add(projectId: UUID, state: TaskState) = executeInIO {
        stateDataSource.add(projectId, state.toDto())
    }

    override fun delete(projectId: UUID, stateId: UUID) = executeInIO {
        stateDataSource.delete(projectId, stateId)
    }
}