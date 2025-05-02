package logic.usecases.state

import logic.models.TaskState
import logic.repositoies.StateRepository
import utilities.IllegalStateTitle

class AddStateUseCase(
    private val stateRepository: StateRepository,
) {

    operator fun invoke(state: TaskState): Boolean {
        val isValid = isValidTitleState(state.title)
        return if (isValid) stateRepository.add(state.projectId, state = state) else {
            throw IllegalStateTitle("Title Task State is not valid")
        }
    }

    private fun isValidTitleState(title: String): Boolean {
        return !(title.isNotBlank() && title.isNotEmpty())
    }
}