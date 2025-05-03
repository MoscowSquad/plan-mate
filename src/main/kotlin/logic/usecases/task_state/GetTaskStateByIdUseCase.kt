package logic.usecases.task_state

import logic.models.TaskState
import logic.repositories.TaskStateRepository
import java.util.*

class GetTaskStateByIdUseCase(
    private val stateRepository: TaskStateRepository
) {
    operator fun invoke(id: UUID): TaskState {
        return TaskState(UUID.randomUUID(), " ", UUID.randomUUID())
    }
}