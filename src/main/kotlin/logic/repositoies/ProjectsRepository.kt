package logic.repositoies

import logic.models.Project
import java.util.UUID

interface ProjectsRepository {
    fun getProjectById(projectId: UUID): Project
    fun saveProject(project: Project)
    fun deleteProject(projectId: UUID)
}