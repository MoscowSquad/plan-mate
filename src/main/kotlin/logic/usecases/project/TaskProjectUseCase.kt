package logic.usecases.project

import logic.models.Task
import logic.repositoies.project.exception.ProjectException.TaskNotFoundException
import logic.repositoies.project.ProjectsRepository
import logic.repositoies.project.TaskProjectRepository
import logic.repositoies.project.exception.validateProjectExists
import java.util.*

class TaskProjectUseCase(
    private val taskProjectRepository: TaskProjectRepository
) {

    fun getSpecificTaskByProjectId(projectId: UUID) : Task? {
         validateProjectExists(projectId)
        return taskProjectRepository.getSpecificTaskByProjectId(projectId)
    }

    fun getAllTasksByProjectId(projectId : UUID): List<Task> {
        validateProjectExists(projectId)
        return taskProjectRepository.getAllTasksByProjectId(projectId) ?:
        throw TaskNotFoundException("Task with ID $projectId was not found.")
    }
}