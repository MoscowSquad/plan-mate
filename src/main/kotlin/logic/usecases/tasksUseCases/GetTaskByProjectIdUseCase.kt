package logic.usecases.tasksUseCases

import logic.models.Task
import logic.repositoies.TasksRepository
import utilities.PropertyNullException
import utilities.TaskIsNotFoundException
import java.util.*

class GetTaskByProjectIdUseCase(
    private val tasksRepository: TasksRepository
) {
    fun getTaskByProjectId(id: UUID?): Task {
        val allTasks = tasksRepository.getAll()
        val projectID = id ?: throw PropertyNullException()

        return allTasks.find { it.projectId == id } ?: throw TaskIsNotFoundException(projectID)
    }
}