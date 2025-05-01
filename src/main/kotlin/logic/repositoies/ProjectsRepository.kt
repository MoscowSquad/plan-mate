package logic.repositoies

import logic.models.Project
import java.util.UUID

interface ProjectsRepository {
    fun save(project: Project): Project
    fun findById(id: UUID): Project?
    fun delete(id: UUID): Boolean
    fun assignUserToProject(projectId: UUID, userId: UUID): Boolean
}