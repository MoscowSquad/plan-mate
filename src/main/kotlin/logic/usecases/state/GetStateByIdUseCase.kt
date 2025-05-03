package logic.usecases.state

import logic.models.TaskState
import logic.repositories.StateRepository
import java.util.*

class GetStateByIdUseCase(
    private val stateRepository: StateRepository
) {
    operator fun invoke(id: UUID): TaskState{
        return stateRepository.getById(id)
    }
}
