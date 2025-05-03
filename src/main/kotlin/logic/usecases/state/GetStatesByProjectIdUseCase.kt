package logic.usecases.state

import logic.models.TaskState
import logic.repositories.StateRepository
import java.util.UUID

class GetStatesByProjectIdUseCase(
    private val stateRepository: StateRepository
) {

    operator fun invoke(projectId: UUID): List<TaskState> {
        return stateRepository.getByProjectId(projectId)
    }
}