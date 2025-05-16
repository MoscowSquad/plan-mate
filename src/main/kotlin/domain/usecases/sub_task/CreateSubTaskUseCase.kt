package domain.usecases.sub_task

import domain.models.SubTask
import domain.repositories.SubTaskRepository

class CreateSubTaskUseCase(
    private val subTaskRepository: SubTaskRepository
) {
    suspend operator fun invoke(subTask: SubTask): Boolean {
        return subTaskRepository.createSubTask(subTask)
    }
}