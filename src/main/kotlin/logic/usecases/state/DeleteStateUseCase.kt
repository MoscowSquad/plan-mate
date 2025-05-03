package logic.usecases.state

import logic.models.TaskState
import logic.repositories.StateRepository
import utilities.NoStateExistException
import java.util.*

class DeleteStateUseCase(
    private val stateRepository: StateRepository,
) {

    operator fun invoke(stateId: UUID, projectId: UUID): Boolean {
        getState(stateId, projectId)
        return stateRepository.delete(projectId, stateId)
            .also { success ->
                if (!success) throw IllegalStateException("Deletion failed unexpectedly")
            }
    }

    private fun getState(stateId: UUID, projectId: UUID): TaskState {
        return stateRepository.getByProjectId(projectId)
            .firstOrNull { it.id == stateId }
            ?: throw NoStateExistException("State with ID $stateId does not exist in project $projectId")
    }

}