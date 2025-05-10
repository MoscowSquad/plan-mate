package presentation.user

import presentation.io.ConsoleIO

class UserUI(
    private val createUserUI: CreateUserUI,
    private val getAllUserUI: GetAllUserUI,
    private val assignProjectToUserUI: AssignProjectToUserUI,
    private val getUserByIdUI: GetUserByIdUI,
    private val deleteUserUI: DeleteUserUI,
    private val consoleIO: ConsoleIO,
) : ConsoleIO by consoleIO {

    operator fun invoke() {
        getAllUserUI()
        while (true) {
            write("\n========== User Management ==========")
            write("1. ➕ Add User")
            write("2. 🗑️  Delete User")
            write("3. 📎 Assign Project to User")
            write("4. 🔍 Get User Details by ID")
            write("5. 🔙 Back")
            write("Enter your option (1–5): ")

            val option = read().toIntOrNull()
            when (option) {
                1 -> createUserUI()
                2 -> deleteUserUI()
                3 -> assignProjectToUserUI()
                4 -> getUserByIdUI()
                5 -> {
                    write("🔙 Returning to the previous menu...")
                    return
                }

                else -> write("❌ Invalid input. Please enter a number between 1 and 5.")
            }
        }
    }
}
