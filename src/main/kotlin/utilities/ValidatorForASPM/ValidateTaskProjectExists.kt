package utilities.ValidatorForASPM

import logic.repositoies.adminSpecificProjectManagmanetRepository.TaskProjectRepository
import utilities.exception.ProjectException.TaskNotFoundException
import java.util.*

class ValidateTaskProjectExists(private val taskRepository: TaskProjectRepository) {
    fun validateTaskExists(projectId: UUID, taskId: UUID) {
        if (!taskRepository.taskExists(projectId, taskId)) {
            throw TaskNotFoundException(taskId.toString())
        }
    }
}