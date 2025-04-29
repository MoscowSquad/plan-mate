package logic.usecases.project

import logic.models.Task
import logic.repositoies.project.exception.ProjectException.TaskNotFoundException
import logic.repositoies.project.TaskProjectRepository
import logic.repositoies.project.exception.ValidateProjectExists
import java.util.*

class TaskProjectUseCase(
    private val taskProjectRepository: TaskProjectRepository,
    private val validateProjectExists: ValidateProjectExists
) {

    fun getSpecificTaskByProjectId(projectId: UUID) : Task? {
        validateProjectExists.isValidById(projectId)
        return taskProjectRepository.getSpecificTaskByProjectId(projectId)
    }

    fun getAllTasksByProjectId(projectId : UUID): List<Task> {
        validateProjectExists.isValidById(projectId)
        return taskProjectRepository.getAllTasksByProjectId(projectId) ?:
        throw TaskNotFoundException("Task with ID $projectId was not found.")
    }
}