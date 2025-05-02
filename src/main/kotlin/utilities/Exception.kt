package utilities


import java.util.UUID


class UnauthorizedAccessException(message: String) : Exception(message)
class NoExistProjectException(projectId: UUID) : Exception("Project '$projectId' does not exist")
class NotAdminException(message: String) : Exception(message)
class InvalidProjectNameException(message: String) : Exception(message)
class ProjectCreationFailedException(message: String) : Exception(message)
class ProjectNotFoundException(id: UUID) : NoSuchElementException("Project with id $id not found")
class NoStateExistException(message: String) : Exception(message)
class IllegalStateTitle(message: String) : Exception(message)
class TaskIsNotFoundException(taskId:UUID) : Exception("Task '$taskId' does not exist")
class TaskIsExist(taskId: UUID) : Exception("Task '$taskId' is exist")

