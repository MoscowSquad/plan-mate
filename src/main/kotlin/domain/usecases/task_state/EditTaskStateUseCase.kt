package domain.usecases.task_state

import domain.models.TaskState
import domain.repositories.TaskStateRepository
import domain.util.IllegalStateTitle
import domain.util.NoStateExistException

class EditTaskStateUseCase(
    private val stateRepository: TaskStateRepository
) {

    suspend operator fun invoke(state: TaskState): TaskState {

        require(isValidTitle(state.name)) {
            throw IllegalStateTitle()
        }

        return if (stateRepository.updateTaskState(state)) {
            state
        } else {
            throw NoStateExistException()
        }
    }

    private fun isValidTitle(title: String): Boolean = title.isNotBlank()
}