package logic.usecases

import logic.models.State
import logic.repositoies.StateRepository
import utilities.IllegalStateTitle

class EditStateUseCase(
    private val stateRepository: StateRepository
){

    operator fun invoke(state: State, title: String): State {
        return state.copy(title = title) // fake for now
    }

    private fun isValidTitle(title: String) {
        if (title.isBlank()) {
            throw IllegalStateTitle("State title cannot be blank")
        }
    }

}