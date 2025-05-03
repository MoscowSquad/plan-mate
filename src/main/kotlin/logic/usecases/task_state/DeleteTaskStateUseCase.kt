package logic.usecases.task_state

import logic.models.TaskState
import logic.repositories.TaskStateRepository
import logic.util.NoStateExistException
import java.util.*

class DeleteTaskStateUseCase(
    private val stateRepository: TaskStateRepository
) {

    operator fun invoke(stateId: UUID, projectId: UUID): Boolean {
        getState(stateId, projectId)
        return stateRepository.deleteTaskState(projectId, stateId)
            .also { success ->
                if (!success) throw IllegalStateException("Deletion failed unexpectedly")
            }
    }

    private fun getState(stateId: UUID, projectId: UUID): TaskState {
        return stateRepository.getTaskStateByProjectId(projectId)
            .firstOrNull { it.id == stateId }
            ?: throw NoStateExistException("State with ID $stateId does not exist in project $projectId")
    }

}