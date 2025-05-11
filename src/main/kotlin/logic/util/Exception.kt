package logic.util

import java.util.*

class UnauthorizedAccessException(message: String) : Exception(message)
class NoExistProjectException(projectId: UUID) : Exception("Project '$projectId' does not exist")
class NotAdminException(message: String) : Exception(message)
class InvalidProjectNameException(message: String) : Exception(message)
class ProjectCreationFailedException(message: String) : Exception(message)
class ProjectNotFoundException(id: UUID) : NoSuchElementException("Project with id $id not found")
class NoStateExistException(message: String) : Exception(message)
class IllegalStateTitle(message: String) : Exception(message)
class TaskIsNotFoundException(taskId: UUID) : Exception("Task '$taskId' does not exist")
class TaskIsExist(taskId: UUID) : Exception("Task '$taskId' is exist")
class UserNotFoundException(message: String) : Exception("User '$message' does not exist")
class InvalidUserCreation(message: String) : Exception("User $message cannot be created")
class UserNotLoggedInException(message: String) : Exception(message)
