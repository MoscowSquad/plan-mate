package logic.repositoies

import logic.models.Task
import java.util.UUID

interface TaskProjectRepository {
    fun getByProjectId(projectId: UUID, taskId: UUID) : Task?
    fun getAll(projectId : UUID) : List<Task>
}