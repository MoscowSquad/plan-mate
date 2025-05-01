package data.repositories

import logic.models.TaskState
import logic.repositoies.StateRepository
import java.util.*

class StateRepositoryImp: StateRepository {
    override fun getStateById(id: UUID): TaskState? {
        return TaskState(UUID.randomUUID(),"name", UUID.randomUUID()) // fake to TDD
    }

    override fun getStates(): List<TaskState> {
        return emptyList() // fake to TDD
    }

    override fun getStatesByProject(projectId: UUID): List<TaskState> {
        return emptyList()
    }

    override fun updateStateTitle(projectId: UUID, stateId: UUID, title: String): Boolean {
        return false // fake to TDD
    }

    override fun addState(projectId: UUID, title: String): Boolean {
        return false // fake to TDD
    }

    override fun deleteState(projectId: UUID, stateId: UUID): Boolean {
        return false
    }

}