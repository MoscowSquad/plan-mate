package logic.usecases

import logic.models.State
import java.util.UUID

class DeleteStateUseCase {

    operator fun invoke(stateId: UUID): Boolean{
        return false
    }

    private fun isStateExist(stateId: UUID): Boolean{
        return false
    }

    private fun getState(stateId: UUID): State {
        return State(id = stateId, title = "nre", projectId = UUID.randomUUID()) // fake return
    }

}