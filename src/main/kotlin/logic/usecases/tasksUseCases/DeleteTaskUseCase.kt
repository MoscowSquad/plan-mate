package logic.usecases.tasksUseCases

import logic.repositoies.TasksRepository
import java.util.*

class DeleteTaskUseCase(
    private val tasksRepository: TasksRepository
) {

    fun deleteTask(id: UUID): Boolean {
        return tasksRepository.deleteTask(id)
    }

}