package data.csv_data.repositories

import data.csv_data.datasource.TaskStateDataSource
import data.csv_data.mappers.toDto
import data.csv_data.mappers.toTaskState
import domain.models.TaskState
import domain.repositories.TaskStateRepository
import domain.util.NoStateExistException
import java.util.*

class TaskStateRepositoryImpl(
    private val stateDataSource: TaskStateDataSource
) : TaskStateRepository {

    val states = mutableListOf<TaskState>()

    init {
        states.addAll(stateDataSource.fetch().map { it.toTaskState() })
    }

    override suspend fun getTaskStateById(id: UUID): TaskState {
        return states.find { it.id == id } ?: throw NoStateExistException()
    }

    override suspend fun getTaskStateByProjectId(projectId: UUID): List<TaskState> {
        return states.filter { it.projectId == projectId }
            .ifEmpty { throw NoStateExistException() }
    }


    override suspend fun updateTaskState(state: TaskState): Boolean {
        val index = states.indexOfFirst { it.id == state.id }
        if (index == -1) return false

        states[index] = state
        stateDataSource.save(states.map { it.toDto() })
        return true
    }

    override suspend fun addTaskState(projectId: UUID, state: TaskState): Boolean {
        val added = states.add(state)
        if (added) stateDataSource.save(states.map { it.toDto() })
        return added
    }

    override suspend fun deleteTaskState(projectId: UUID, stateId: UUID): Boolean {
        val removed = states.removeIf { it.id == stateId && it.projectId == projectId }
        if (removed) stateDataSource.save(states.map { it.toDto() })
        return removed
    }
}
