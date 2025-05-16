package domain.usecases.task_state

import domain.models.TaskState
import domain.repositories.TaskStateRepository
import java.util.*

class GetTaskStateByIdUseCase(
    private val stateRepository: TaskStateRepository
) {
    suspend operator fun invoke(id: UUID): TaskState {
        return stateRepository.getTaskStateById(id)
    }
}
