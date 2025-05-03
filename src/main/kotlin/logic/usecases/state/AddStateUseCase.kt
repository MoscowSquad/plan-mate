package logic.usecases.state

import logic.models.TaskState
import logic.repositories.StateRepository
import utilities.IllegalStateTitle

class AddStateUseCase(
    private val stateRepository: StateRepository,
) {

    operator fun invoke(state: TaskState): Boolean {
        require(isValidTitle(state.title)) {
            throw IllegalStateTitle("Task state title cannot be blank")
        }

        return stateRepository.add(state.projectId, state)
            .also { success ->
                if (!success) throw IllegalStateException("Failed to add state")
            }
    }

    private fun isValidTitle(title: String): Boolean {
        return title.isNotBlank() && title.length in 1..100
    }
}