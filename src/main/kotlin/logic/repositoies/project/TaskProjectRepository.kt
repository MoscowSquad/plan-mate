package logic.repositoies.project

import logic.models.Task
import java.util.UUID

interface TaskProjectRepository {
    fun getSpecificTaskByProjectId(projectId: UUID, taskId: UUID) : Task?
    fun getAllTasksByProjectId(projectId : UUID) : List<Task>
    fun taskExists(projectId: UUID, taskId: UUID): Boolean

}