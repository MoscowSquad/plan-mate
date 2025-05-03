package logic.repositories

import logic.models.TaskState
import java.util.*

interface TaskStateRepository {
    fun getTaskStateById(id: UUID): TaskState
    fun getTaskStateByProjectId(projectId: UUID): List<TaskState>
    fun updateTaskState(state: TaskState): Boolean
    fun addTaskState(projectId: UUID, title: String): Boolean
    fun deleteTaskState(projectId: UUID, stateId: UUID): Boolean

}