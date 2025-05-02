package data.repositories

import data.datasource.TaskStateDataSource
import logic.models.TaskState
import logic.repositories.StateRepository
import java.util.*

class StateRepositoryImpl(
    private val stateDataSource: TaskStateDataSource
) : StateRepository {

    private val states = mutableListOf<TaskState>()

    init {
        states.addAll(stateDataSource.fetch())
    }

    override fun getById(id: UUID): TaskState {
        return states.find { it.id == id } ?: throw NoSuchElementException("State with ID $id not found.")
    }

    override fun getByProjectId(projectId: UUID): List<TaskState> {
        return states.filter { it.projectId == projectId }
    }

    override fun update(state: TaskState): Boolean {
        val index = states.indexOfFirst { it.id == state.id }
        if (index == -1) return false

        states[index] = state
        stateDataSource.save(states)
        return true
    }

    override fun add(projectId: UUID, title: String): Boolean {
        val newState = TaskState(UUID.randomUUID(), title, projectId)
        val added = states.add(newState)
        if (added) stateDataSource.save(states)
        return added
    }

    override fun delete(projectId: UUID, stateId: UUID): Boolean {
        val removed = states.removeIf { it.id == stateId && it.projectId == projectId }
        if (removed) stateDataSource.save(states)
        return removed
    }
}
