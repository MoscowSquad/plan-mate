package presentation.project

import presentation.io.ConsoleIO

class ProjectsUI(
    private val getAllProjectsUI: GetAllProjectsUI,
    private val createProjectUI: CreateProjectUI,
    private val updateProjectNameUI: UpdateProjectNameUI,
    private val deleteProjectUI: DeleteProjectUI,
    private val consoleIO: ConsoleIO,
) : ConsoleIO by consoleIO {
    suspend operator fun invoke() {
        getAllProjectsUI()

        write(
            """
        📁 Projects Menu:
        1️. ➕ Create a New Project  
        2️. ✏️ Update Project Name
        3️. ❌ Delete a Project  
        4️. 🔙 Back to Main Menu

        Enter an option:
        """.trimIndent()
        )
        val input = read().toIntOrNull()

        when (input) {
            1 -> createProjectUI()
            2 -> updateProjectNameUI()
            3 -> deleteProjectUI()
            4 -> {
                write("Going back to the main menu...")
                return
            }
            else -> write("\nInvalid input. Please enter a number between 1 and 4.")
        }
    }
}