package logic.usecases.task_state

import logic.models.TaskState
import logic.repositories.TaskStateRepository
import java.util.UUID

class GetTaskStatesByProjectIdUseCase(
    private val stateRepository: TaskStateRepository
) {

    operator fun invoke(projectId: UUID): List<TaskState>{
        return stateRepository.getTaskStateByProjectId(projectId)
    }
}