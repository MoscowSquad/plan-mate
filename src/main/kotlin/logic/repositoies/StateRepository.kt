package logic.repositoies

import logic.models.State
import java.util.UUID

interface StateRepository {
    fun getStateById(id: UUID): State?
    fun getStates(): List<State>
    fun getStatesByProject(projectId: UUID): List<State>
    fun updateStateTitle(projectId: UUID, stateId: UUID, title: String): Boolean
    fun addState(projectId: UUID, title: String): Boolean
    fun deleteState(projectId: UUID, stateId: UUID): Boolean

}