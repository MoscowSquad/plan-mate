package domain.usecases.task

import domain.repositories.TasksRepository
import java.util.*

class DeleteTaskUseCase(
    private val tasksRepository: TasksRepository
) {
    suspend operator fun invoke(id: UUID): Boolean {
        return tasksRepository.deleteTask(id)
    }
}