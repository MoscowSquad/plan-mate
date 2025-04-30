package data.repositories

import logic.models.State
import logic.repositoies.ProjectsRepository
import java.util.*

class ProjectsRepositoryImpl: ProjectsRepository {
    override fun deleteState(projectId: UUID, state: UUID): Boolean {
        TODO("Not yet implemented")
    }

    override fun getStateById(stateId: UUID): State {
        TODO("Not yet implemented")
    }

    override fun getStatesById(projectId: UUID): List<State> {
        TODO("Not yet implemented")
    }
}