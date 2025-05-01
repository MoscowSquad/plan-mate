package utilities

import java.util.UUID

class StateNotExistException(message: String) : Exception(message)
class NoExistProjectException(projectId: UUID) : Exception("Project '$projectId' does not exist")

