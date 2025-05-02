package logic.usecases.tasksUseCases

import logic.repositoies.TasksRepository
import java.util.*

class DeleteTaskUseCase(
    private val tasksRepository: TasksRepository
) {

    operator fun invoke(id: UUID): Boolean {
        return tasksRepository.delete(id)
    }

}