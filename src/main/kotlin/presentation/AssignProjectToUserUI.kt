import presentation.io.ConsoleIO
import logic.models.UserRole
import logic.usecases.user.AssignProjectToUserUseCase
import utilities.UnauthorizedAccessException
import java.util.UUID

class AssignProjectToUserUI(
    private val assignProjectToUserUseCase: AssignProjectToUserUseCase,
    private val currentUserRole: UserRole,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {

    operator fun invoke() {
            write("\n=== Assign Project to User ===")

            write("Enter Project ID:")
            val projectIdInput = read()
            val projectId = UUID.fromString(projectIdInput)

            write("Enter User ID:")
            val userIdInput = read()
            val userId = UUID.fromString(userIdInput)

            val success = assignProjectToUserUseCase(currentUserRole, projectId, userId)

            if (success) {
                write("User successfully assigned to the project.")
            } else {
                write("Failed to assign user. Make sure IDs are correct.")
            }
    }
}
