package logic.repositoies

import logic.models.TaskState
import java.util.*

interface StateRepository {
    fun getById(id: UUID): TaskState
    fun getByProjectId(projectId: UUID): List<TaskState>
    fun update(state: TaskState): Boolean
    fun add(projectId: UUID, state: TaskState): Boolean
    fun delete(projectId: UUID, stateId: UUID): Boolean

}