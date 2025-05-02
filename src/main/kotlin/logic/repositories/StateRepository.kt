package logic.repositories

import logic.models.TaskState
import java.util.*

interface StateRepository {
    fun getById(id: UUID): TaskState
    fun getByProjectId(projectId: UUID): List<TaskState>
    fun update(state: TaskState): Boolean
    fun add(projectId: UUID, title: String): Boolean
    fun delete(projectId: UUID, stateId: UUID): Boolean

}