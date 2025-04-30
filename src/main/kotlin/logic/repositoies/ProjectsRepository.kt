package logic.repositoies

import java.util.UUID

interface ProjectsRepository {

    fun updateStateTitleForSpecificProjectById(projectId: UUID, stateId: UUID)

}