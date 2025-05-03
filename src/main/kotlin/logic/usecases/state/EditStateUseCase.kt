package logic.usecases.state

import logic.models.TaskState
import logic.repositories.StateRepository
import utilities.IllegalStateTitle
import utilities.NoStateExistException

class EditStateUseCase(
    private val stateRepository: StateRepository
) {

    operator fun invoke(state: TaskState): TaskState {
        require(isValidTitle(state.title)) {
            throw IllegalStateTitle("TaskState title cannot be blank")
        }

        return if (stateRepository.update(state)) {
            state
        } else {
            throw NoStateExistException("State with ID ${state.id} not found")
        }
    }

    private fun isValidTitle(title: String): Boolean = title.isNotBlank()


}