package logic.repositoies

import logic.models.TaskState
import java.util.*

interface StatesRepository {
    fun deleteState(projectId: UUID, stateId: UUID): Boolean
    fun getStateById(stateId: UUID): TaskState
}