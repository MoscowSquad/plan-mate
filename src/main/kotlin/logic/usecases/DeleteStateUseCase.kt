package logic.usecases

import logic.models.TaskState
import logic.repositoies.ProjectsRepository
import java.util.*

class DeleteStateUseCase(
    private val stateRepository: ProjectsRepository
) {

    operator fun invoke(stateId: UUID, projectId: UUID): Boolean{
        return true
    }

    private fun isStateExist(stateId: UUID): Boolean{
        return false
    }

    private fun getState(stateId: UUID): TaskState {
        return TaskState(id = stateId, title = "nre", projectId = UUID.randomUUID()) // fake return
    }

}