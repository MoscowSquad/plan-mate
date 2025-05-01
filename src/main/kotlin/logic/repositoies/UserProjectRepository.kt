package logic.repositoies

import logic.models.User
import java.util.UUID

interface UserProjectRepository {
    fun getByTaskId(projectId: UUID, taskId: UUID) : User?
    fun getByProjectId(projectId: UUID): List<User>?
}
