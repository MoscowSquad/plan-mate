package domain.usecases.sub_task

import domain.repositories.SubTaskRepository
import java.util.*

class DeleteSubTaskUseCase(
    private val subTaskRepository: SubTaskRepository
) {
    suspend operator fun invoke(subTaskId: UUID): Boolean {
        return subTaskRepository.deleteSubTask(subTaskId)
    }
}