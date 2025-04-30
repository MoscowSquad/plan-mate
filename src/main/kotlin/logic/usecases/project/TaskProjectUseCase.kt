package logic.usecases.project

import logic.models.Task
import logic.repositoies.project.TaskProjectRepository
import utilities.exception.ProjectException.TaskNotFoundException
import utilities.exception.ValidateProjectExists
import utilities.exception.ValidateTaskProjectExists
import java.util.*

class TaskProjectUseCase(
    private val taskRepository: TaskProjectRepository,
    private val validateProjectExists: ValidateProjectExists,
    private val validateTaskExists: ValidateTaskProjectExists
) {
    fun getSpecificTaskByProjectId(projectId: UUID, taskId: UUID): Task {
        validateProjectExists.validateProjectExists(projectId)
        validateTaskExists.validateTaskExists(projectId, taskId)
        return taskRepository.getSpecificTaskByProjectId(projectId, taskId)
            ?: throw TaskNotFoundException(taskId.toString())
    }

    fun getAllTasksByProjectId(projectId: UUID): List<Task> {
        validateProjectExists.validateProjectExists(projectId)
        return taskRepository.getAllTasksByProjectId(projectId)
    }

}