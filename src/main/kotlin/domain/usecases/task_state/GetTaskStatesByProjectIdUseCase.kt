package domain.usecases.task_state

import domain.models.TaskState
import domain.repositories.TaskStateRepository
import java.util.*

class GetTaskStatesByProjectIdUseCase(
    private val stateRepository: TaskStateRepository
) {
    suspend operator fun invoke(projectId: UUID): List<TaskState> {
        return stateRepository.getTaskStateByProjectId(projectId)
    }
}