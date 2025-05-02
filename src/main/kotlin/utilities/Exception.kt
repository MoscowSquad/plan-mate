package utilities

import java.util.UUID

class NoExistProjectException(projectId: UUID) : Exception("Project '$projectId' does not exist")

class UnauthorizedAccessException(message: String) : Exception(message)