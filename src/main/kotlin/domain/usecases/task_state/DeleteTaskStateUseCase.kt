package domain.usecases.task_state

import domain.models.TaskState
import domain.repositories.TaskStateRepository
import domain.util.NoStateExistException
import java.util.*

class DeleteTaskStateUseCase(
    private val stateRepository: TaskStateRepository
) {
    suspend operator fun invoke(stateId: UUID, projectId: UUID): Boolean {
        getState(stateId, projectId)
        return stateRepository.deleteTaskState(projectId, stateId)
            .also { success ->
                if (!success) throw IllegalStateException()
            }
    }

    private suspend fun getState(stateId: UUID, projectId: UUID): TaskState {
        return stateRepository.getTaskStateByProjectId(projectId)
            .firstOrNull { it.id == stateId }
            ?: throw NoStateExistException()
    }
}