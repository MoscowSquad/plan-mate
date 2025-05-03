package logic.usecases.task_state

import logic.models.TaskState
import logic.repositories.TaskStateRepository

class AddTaskStateUseCase(
    private val stateRepository: TaskStateRepository,
) {

    operator fun invoke(state: TaskState): Boolean{
        isValidTitleState(state.name)
        return false// fake
    }

    private fun isValidTitleState(title: String): Boolean{
        return false
    }
}