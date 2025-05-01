package data.repositories

import logic.models.State
import logic.repositoies.StateRepository
import java.util.*

class StateRepositoryImp: StateRepository {
    override fun getStateById(id: UUID): State? {
        return State(UUID.randomUUID(),"name", UUID.randomUUID()) // fake to TDD
    }

    override fun getStates(): List<State> {
        return emptyList() // fake to TDD
    }

    override fun getStatesByProject(projectId: UUID): List<State> {
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