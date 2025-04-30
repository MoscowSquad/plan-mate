package logic.repositoies

import logic.models.State
import java.util.UUID

interface ProjectsRepository {

    fun deleteState(projectId: UUID, state: UUID): Boolean

    fun getStateById(stateId: UUID): State

    fun getStatesById(projectId: UUID): List<State>
}