package logic.usecases.task_state

import logic.models.TaskState
import logic.repositories.TaskStateRepository
import logic.util.IllegalStateTitle

class AddTaskStateUseCase(
    private val stateRepository: TaskStateRepository,
) {

    operator fun invoke(state: TaskState): Boolean {
        require(isValidTitle(state.name)) {
            throw IllegalStateTitle("Task state title cannot be blank")
        }

        return stateRepository.addTaskState(state.projectId, state)
            .also { success ->
                if (!success) throw IllegalStateException("Failed to add state")
            }
    }

    private fun isValidTitle(title: String): Boolean {
        return title.isNotBlank() && title.length <= 100
    }
}