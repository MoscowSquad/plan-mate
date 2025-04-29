package logic.repositoies.project

import logic.models.Task
import java.util.UUID

interface TaskProjectRepository {
    fun getSpecificTaskByProjectId(projectId: UUID) : Task?
    fun getAllTasksByProjectId(projectId : UUID) : List<Task>?
}