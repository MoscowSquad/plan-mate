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
        write(
            """
1. add user
2. delete user
3. change user role
4. get user details by id
5. get all users
6. back

Enter your option:
        """.trimIndent()
        )
        val option = read().toIntOrNull()
        when (option) {
            1 -> createUserUI()
            2 -> deleteUserUI()
            3 -> assignProjectToUserUI()
            4 -> getUserByIdUI()
            5 -> getAllUserUI()
            6 -> return
            else -> write("Wrong option enter a number between 1 and 6.")
        }
    }
}