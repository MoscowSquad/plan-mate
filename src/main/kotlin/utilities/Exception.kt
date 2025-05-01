package utilities

import java.util.*


class NoExistProjectException(projectId: UUID) : Exception("Project '$projectId' does not exist")
class IllegalStateTitle(message: String): Exception(message)