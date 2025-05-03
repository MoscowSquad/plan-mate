package logic.usecases.task_state

import logic.models.TaskState
import logic.repositories.TaskStateRepository
import logic.util.IllegalStateTitle

class EditTaskStateUseCase(
    private val stateRepository: TaskStateRepository
){

    operator fun invoke(state: TaskState): TaskState {
        return state.copy(name = state.name) // fake for now
    }

    private fun isValidTitle(title: String) {
        if (title.isBlank()) {
            throw IllegalStateTitle("State title cannot be blank")
        }
    }

}