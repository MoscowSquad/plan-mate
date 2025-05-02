package utilities

import java.util.UUID

class NoStateExistException(message: String) : Exception(message)
class NoExistProjectException(projectId: UUID) : Exception("Project '$projectId' does not exist")
class IllegalStateTitle(message: String): Exception(message)
class UnauthorizedAccessException(message: String) : Exception(message)
