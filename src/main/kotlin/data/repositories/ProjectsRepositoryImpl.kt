package data.repositories

import logic.models.Project
import logic.repositories.ProjectsRepository
import java.util.UUID

class ProjectsRepositoryImpl: ProjectsRepository {
    override fun getProjectById(projectId: UUID): Project {
        TODO("Not yet implemented")
    }

    override fun saveProject(project: Project) {
        TODO("Not yet implemented")
    }

    override fun deleteProject(projectId: UUID) {
        TODO("Not yet implemented")
    }
}