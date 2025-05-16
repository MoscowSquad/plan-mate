package domain.usecases.sub_task


import domain.models.SubTask
import domain.repositories.SubTaskRepository
import java.util.*

class GetSubTasksByTaskIdUseCase(
    private val subTaskRepository: SubTaskRepository
) {
    suspend operator fun invoke(taskId: UUID): List<SubTask> {
        return subTaskRepository.getSubTasksByTaskId(taskId)
    }
}