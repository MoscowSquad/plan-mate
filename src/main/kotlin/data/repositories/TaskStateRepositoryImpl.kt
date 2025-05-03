package data.repositories

import data.datasource.TaskStateDataSource
import data.mappers.toDto
import data.mappers.toTaskState
import logic.models.TaskState
import logic.repositories.TaskStateRepository
import java.util.*

class TaskStateRepositoryImpl(
    private val stateDataSource: TaskStateDataSource
) : TaskStateRepository {

    private val states = mutableListOf<TaskState>()

    init {
        states.addAll(stateDataSource.fetch().map { it.toTaskState() })
    }

    override fun getTaskStateById(id: UUID): TaskState {
        return states.find { it.id == id } ?: throw NoSuchElementException("State with ID $id not found.")
    }

    override fun getTaskStateByProjectId(projectId: UUID): List<TaskState> {
        return states.filter { it.projectId == projectId }
    }

    override fun updateTaskState(state: TaskState): Boolean {
        val index = states.indexOfFirst { it.id == state.id }
        if (index == -1) return false

        states[index] = state
        stateDataSource.save(states.map { it.toDto() })
        return true
    }

    override fun addTaskState(projectId: UUID, title: String): Boolean {
        val newState = TaskState(UUID.randomUUID(), title, projectId)
        val added = states.add(newState)
        if (added) stateDataSource.save(states.map { it.toDto() })
        return added
    }

    override fun deleteTaskState(projectId: UUID, stateId: UUID): Boolean {
        val removed = states.removeIf { it.id == stateId && it.projectId == projectId }
        if (removed) stateDataSource.save(states.map { it.toDto() })
        return removed
    }
}
