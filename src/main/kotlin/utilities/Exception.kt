package utilities

import java.util.*

class NoExistProjectException(projectId: UUID) : Exception("Project '$projectId' does not exist")
class TaskIsNotFoundException(taskId:UUID) : Exception("Task '$taskId' does not exist")
class PropertyNullException() : Exception("your input has null value")
class TaskIsExist(taskId: UUID) : Exception("Task '$taskId' is exist")

