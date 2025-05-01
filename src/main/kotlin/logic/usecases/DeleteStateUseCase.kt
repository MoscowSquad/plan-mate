package logic.usecases

import logic.models.State
import logic.repositoies.ProjectsRepository
import java.util.UUID

class DeleteStateUseCase(
    private val stateRepository: ProjectsRepository
) {

    operator fun invoke(stateId: UUID, projectId: UUID): Boolean{
        return true
    }

    private fun isStateExist(stateId: UUID): Boolean{
        return false
    }

    private fun getState(stateId: UUID): State {
        return State(id = stateId, title = "nre", projectId = UUID.randomUUID()) // fake return
    }

}