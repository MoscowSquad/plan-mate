package logic.usecases

import logic.models.Task
import logic.repositoies.TaskProjectRepository
import utilities.exception.ProjectException.TaskNotFoundException
import java.util.*

class TaskProjectUseCase(
    private val taskRepository: TaskProjectRepository,
    private val projectExistenceValidator: ProjectExistenceValidator,

    ) {
    fun getByProjectId(projectId: UUID, taskId: UUID): Task {
        projectExistenceValidator.isExist(projectId)

        return taskRepository.getByProjectId(projectId, taskId)
            ?: throw TaskNotFoundException(taskId.toString())
    }

    fun getAllByProjectId(projectId: UUID): List<Task> {
        projectExistenceValidator.isExist(projectId)
        return taskRepository.getAll(projectId)
    }

}