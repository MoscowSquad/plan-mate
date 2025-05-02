package data.repositories

import data.datasource.TaskStateDataSource
import logic.models.TaskState
import logic.repositoies.StateRepository
import utilities.NoExistProjectException
import utilities.NoStateExistException
import java.util.*

class StateRepositoryImpl(
    private val taskStateDataSource: TaskStateDataSource,
) : StateRepository {

    override fun getById(id: UUID): TaskState {
        val taskStates = taskStateDataSource.fetch()
        return taskStates.firstOrNull { state -> id == state.id } ?: throw NoStateExistException("No State Exist")
    }

    override fun getByProjectId(projectId: UUID): List<TaskState> {
        val taskStates = taskStateDataSource.fetch()
        return taskStates
            .filter { it.projectId == projectId }
            .takeIf { it.isNotEmpty() } ?: throw NoExistProjectException(projectId)
    }


    override fun update(state: TaskState): Boolean {
        val taskStates = taskStateDataSource.fetch()
        val taskStateOrNull  = taskStates.firstOrNull { taskState -> taskState == state }
        return false // fake

        }

    override fun add(projectId: UUID, state: TaskState): Boolean {
        return false
    }

    override fun delete(projectId: UUID, stateId: UUID): Boolean {
        return false
    }

}