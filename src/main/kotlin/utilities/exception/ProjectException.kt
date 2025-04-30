package utilities.exception

import java.util.*

sealed class ProjectException(message: String) : Exception(message) {
    class ProjectNotFoundException(message: String) :
        ProjectException("Project with ID $message was not found")

    class ProjectValidationException(message: String) :
        ProjectException("Project validation failed: $message")

    class UnauthorizedProjectAccessException(userId: UUID, projectId: UUID) :
        ProjectException("User $userId is not authorized to access project $projectId")

    class ProjectAlreadyExistsException(projectName: String) :
        ProjectException("Project '$projectName' already exists")

    class TaskNotFoundException(tasktName: String) :
        ProjectException("Task '$tasktName' already exists")
}