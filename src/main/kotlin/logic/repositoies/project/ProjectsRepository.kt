package logic.repositoies.project

import logic.models.Project
import java.util.UUID

interface ProjectsRepository {
    fun getAllProjects(): List<Project>?
    fun getProjectById(projectId: UUID): Project?
}