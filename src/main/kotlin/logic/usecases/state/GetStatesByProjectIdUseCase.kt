package logic.usecases.state

import logic.models.TaskState
import logic.repositoies.StateRepository
import java.util.UUID

class GetStatesByProjectIdUseCase(
    private val stateRepository: StateRepository
) {

    operator fun invoke(projectId: UUID): List<TaskState>{
        return emptyList()
    }
}