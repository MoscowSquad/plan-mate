package logic.repositoies.project

import logic.models.User
import java.util.UUID

interface UserProjectRepository {
    val userProjectRepository = UserProjectRepository
    fun getUsersByProject(projectId: UUID): List<User>?
}