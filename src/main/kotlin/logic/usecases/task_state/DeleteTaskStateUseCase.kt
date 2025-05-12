package logic.usecases.task_state

import logic.models.TaskState
import logic.repositories.TaskStateRepository
import logic.util.NoStateExistException
import logic.util.NotAdminException
import java.util.*

class DeleteTaskStateUseCase(
    private val stateRepository: TaskStateRepository
) {

    operator fun invoke(stateId: UUID, projectId: UUID, isAdmin: Boolean): Boolean {
        require(isAdmin) {
            throw NotAdminException()
        }
        getState(stateId, projectId)
        return stateRepository.deleteTaskState(projectId, stateId)
            .also { success ->
                if (!success) throw IllegalStateException()
            }
    }

    private fun getState(stateId: UUID, projectId: UUID): TaskState {
        return stateRepository.getTaskStateByProjectId(projectId)
            .firstOrNull { it.id == stateId }
            ?: throw NoStateExistException()
    }

}