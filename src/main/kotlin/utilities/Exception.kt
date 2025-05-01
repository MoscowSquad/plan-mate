package utilities

import java.util.UUID


class NoExistProjectException(projectId: UUID) : Exception("Project '$projectId' does not exist")

