package logic.repositoies

import logic.models.TaskState
import java.util.*

interface StateRepository {
    fun getStateById(id: UUID): TaskState?
    fun getStates(): List<TaskState>
    fun getStatesByProject(projectId: UUID): List<TaskState>
    fun updateStateTitle(projectId: UUID, stateId: UUID, title: String): Boolean
    fun addState(projectId: UUID, title: String): Boolean
    fun deleteState(projectId: UUID, stateId: UUID): Boolean

}