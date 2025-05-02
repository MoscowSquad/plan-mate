package logic.usecases.state

import logic.models.TaskState
import logic.repositoies.StateRepository
import java.util.*

class GetStateByIdUseCase(
    private val stateRepository: StateRepository
) {
    operator fun invoke(id: UUID): TaskState{
        return TaskState(UUID.randomUUID(), " ", UUID.randomUUID())
    }
}