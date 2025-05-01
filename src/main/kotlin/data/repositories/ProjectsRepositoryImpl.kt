package data.repositories

import logic.repositoies.ProjectsRepository
import java.util.*

class ProjectsRepositoryImpl: ProjectsRepository {
    override fun isExist(projectId: UUID): Boolean {
        return false
    }
}