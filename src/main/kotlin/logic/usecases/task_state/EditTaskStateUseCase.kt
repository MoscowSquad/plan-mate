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
            throw NotAdminException("Only administrators can edit task states")
        }
        require(isValidTitle(state.name)) {
            throw IllegalStateTitle("TaskState title cannot be blank")
        }

        return if (stateRepository.updateTaskState(state)) {
            state
        } else {
            throw NoStateExistException("State with ID ${state.id} not found")
        }
    }

    private fun isValidTitle(title: String): Boolean = title.isNotBlank()


}