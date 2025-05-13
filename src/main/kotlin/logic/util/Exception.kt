package logic.util

import java.util.*

class UnauthorizedAccessException : Exception("Only admins can create users")
class NoExistProjectException(projectId: UUID) : Exception("Project '$projectId' does not exist")
class NotAdminException : Exception("Only administrators can delete task states")
class InvalidProjectNameException: Exception("Project name cannot be empty")
class ProjectCreationFailedException: Exception("Failed to create project")
class ProjectNotFoundException(id: UUID) : NoSuchElementException("Project with id $id not found")
class NoStateExistException : Exception("State with this ID not found")
class IllegalStateTitle : Exception("Task state title cannot be blank")
class TaskIsNotFoundException(taskId: UUID) : Exception("Task '$taskId' does not exist")
class TaskIsExist(taskId: UUID) : Exception("Task '$taskId' is exist")
class UserNotFoundException(message: String) : Exception("User '$message' does not exist")
class InvalidUserCreation(message: String) : Exception("User $message cannot be created")
class UserNotLoggedInException : Exception("User is not logged in")
