package utilities.exception

sealed class ProjectException(message: String) : Exception(message) {

    class ProjectNotFoundException(message: String) :
        ProjectException("Project with ID $message was not found.")

    class TaskNotFoundException(message: String) :
        ProjectException("Task with ID $message was not found.")

    class UserNotFoundException(message: String) :
        ProjectException("User with ID $message was not found.")


}
