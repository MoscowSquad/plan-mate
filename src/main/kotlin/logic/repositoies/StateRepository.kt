package logic.repositoies

import logic.models.TaskState
import java.util.*

interface StateRepository {
    fun getStateById(id: UUID): TaskState
    fun getById(id: UUID): TaskState
    fun getByProjectId(projectId: UUID): List<TaskState>
    fun update(state: TaskState): Boolean
    fun add(projectId: UUID, title: String): Boolean
    fun delete(projectId: UUID, stateId: UUID): Boolean

}