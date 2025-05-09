package logic.repositories

import logic.models.TaskState
import java.util.*

interface StateRepository {
    fun getStateById(id: UUID): TaskState
    fun getByProjectId(projectId: UUID): List<TaskState>
    fun updateState(state: TaskState): Boolean
    fun addState(projectId: UUID, state: TaskState): Boolean
    fun deleteState(projectId: UUID, stateId: UUID): Boolean

}