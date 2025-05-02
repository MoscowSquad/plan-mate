package logic.usecases

import logic.models.TaskState
import logic.repositoies.StateRepository

class AddStateUseCase(
    private val stateRepository: StateRepository,
) {

    operator fun invoke(state: TaskState): Boolean{
        isValidTitleState(state.title)
        return false// fake
    }

    private fun isValidTitleState(title: String): Boolean{
        return false
    }
}