package logic.usecases.task_state

import logic.models.TaskState
import logic.repositories.StateRepository
import utilities.IllegalStateTitle
import utilities.NoStateExistException
import logic.repositories.TaskStateRepository
import logic.util.IllegalStateTitle

class EditTaskStateUseCase(
    private val stateRepository: TaskStateRepository
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