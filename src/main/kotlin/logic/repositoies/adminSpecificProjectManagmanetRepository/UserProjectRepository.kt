package logic.repositoies.adminSpecificProjectManagmanetRepository

import logic.models.User
import java.util.UUID

interface UserProjectRepository {
    fun getUserByTaskId(projectId: UUID, taskId: UUID) : User?
    fun getUsersByProjectId(projectId: UUID): List<User>?
    fun userExists(projectId: UUID, taskId: UUID, userId: UUID): Boolean

}