package domain.usecases.task_state

import domain.models.TaskState
import domain.repositories.TaskStateRepository
import domain.util.IllegalStateTitle

class AddTaskStateUseCase(
    private val stateRepository: TaskStateRepository,
) {
    suspend operator fun invoke(state: TaskState): Boolean {
        require(isValidTitle(state.name)) {
            throw IllegalStateTitle()
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