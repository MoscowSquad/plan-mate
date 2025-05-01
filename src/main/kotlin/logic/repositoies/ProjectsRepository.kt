package logic.repositoies

import logic.models.State
import java.util.UUID

interface ProjectsRepository {
    fun isExist(projectId: UUID): Boolean
    fun deleteState(projectId: UUID, stateId: UUID): Boolean
    fun getStateById(stateId: UUID): State
}