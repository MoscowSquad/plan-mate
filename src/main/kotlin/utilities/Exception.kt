package utilities
import java.util.UUID
class UnauthorizedAccessException(message: String) : Exception(message)
class NoExistProjectException(projectId: UUID) : Exception("Project '$projectId' does not exist")
class NotAdminException(message: String) : Exception(message)
class InvalidProjectNameException(message: String) : Exception(message)
class ProjectCreationFailedException(message: String) : Exception(message)
class ProjectNotFoundException(id: UUID) : NoSuchElementException("Project with id $id not found")

