package logic.usecases

import logic.models.TaskState
import logic.repositoies.StateRepository
import utilities.IllegalStateTitle

class EditStateUseCase(
    private val stateRepository: StateRepository
){

    operator fun invoke(state: TaskState): TaskState {
        return state.copy(title = state.title) // fake for now
    }

    private fun isValidTitle(title: String) {
        if (title.isBlank()) {
            throw IllegalStateTitle("State title cannot be blank")
        }
    }

}