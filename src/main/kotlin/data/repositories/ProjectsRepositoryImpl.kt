package data.repositories

import logic.models.State
import logic.repositoies.ProjectsRepository
import java.util.*

class ProjectsRepositoryImpl: ProjectsRepository{
    override fun isExist(projectId: UUID): Boolean {
        return false
    }

    override fun addState(state: State): Boolean {
        return false
    }

}