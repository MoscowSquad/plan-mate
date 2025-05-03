package logic.usecases.task_state

import logic.models.TaskState
import logic.repositories.TaskStateRepository
import java.util.*

class DeleteTaskStateUseCase(
    private val stateRepository: TaskStateRepository
) {

    operator fun invoke(stateId: UUID, projectId: UUID): Boolean {
        return true
    }

    private fun getState(stateId: UUID): TaskState {
        return TaskState(id = stateId, name = "nre", projectId = UUID.randomUUID()) // fake return
    }

}