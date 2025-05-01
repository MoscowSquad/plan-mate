package logic.repositoies

import logic.models.TaskState
import java.util.UUID

interface ProjectsRepository {
    fun isExist(projectId: UUID): Boolean
    fun deleteState(projectId: UUID, stateId: UUID): Boolean
    fun getStateById(stateId: UUID): TaskState
}