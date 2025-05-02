package logic.usecases.state

import logic.models.TaskState
import logic.repositoies.StateRepository
import java.util.*

class DeleteStateUseCase(
    private val stateRepository: StateRepository
) {

    operator fun invoke(stateId: UUID, projectId: UUID): Boolean{
        return true
    }

    private fun getState(stateId: UUID): TaskState {
        return TaskState(id = stateId, title = "nre", projectId = UUID.randomUUID()) // fake return
    }

}