package logic.usecases.task

import logic.repositoies.TasksRepository
import java.util.*

class DeleteTaskUseCase(
    private val tasksRepository: TasksRepository
) {

    operator fun invoke(id: UUID): Boolean {
        return tasksRepository.delete(id)
    }

}