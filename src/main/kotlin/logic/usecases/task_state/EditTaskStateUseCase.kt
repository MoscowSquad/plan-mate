package logic.usecases.task_state

import logic.models.TaskState
import logic.repositories.TaskStateRepository
import logic.util.IllegalStateTitle
import logic.util.NoStateExistException
import logic.util.NotAdminException

class EditTaskStateUseCase(
    private val stateRepository: TaskStateRepository
) {

    operator fun invoke(state: TaskState, isAdmin: Boolean): TaskState {
        require(isAdmin) {
            throw NotAdminException()
        }
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