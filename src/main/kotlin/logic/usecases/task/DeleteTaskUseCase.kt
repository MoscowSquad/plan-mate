package logic.usecases.task

import logic.repositories.TasksRepository
import java.util.*

class DeleteTaskUseCase(
    private val tasksRepository: TasksRepository
) {

    operator fun invoke(id: UUID): Boolean {
        return tasksRepository.deleteTask(id)
    }

}