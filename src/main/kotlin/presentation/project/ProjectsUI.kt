package presentation.project

import presentation.io.ConsoleIO

class ProjectsUI(
    private val getAllProjectsUI: GetAllProjectsUI,
    private val createProjectUI: CreateProjectUI,
    private val updateProjectUI: UpdateProjectUI,
    private val deleteProjectUI: DeleteProjectUI,
    private val consoleIO: ConsoleIO,
) : ConsoleIO by consoleIO {
    operator fun invoke() {
        write(
            """
📁 Projects Menu:
1. 📜 show all projects
2. ➕ Create a New Project  
3. ✏️ Update an Existing Project  
4️. ❌ Delete a Project  
5️. 🔙 Back to Main Menu

Enter an option:
        """.trimIndent()
        )
        val input = read().toIntOrNull()

        when (input) {
            1 -> getAllProjectsUI()
            2 -> createProjectUI()
            3 -> updateProjectUI()
            4 -> deleteProjectUI()
            5 -> {
                write("Going back to the main menu...")
                return
            }
            else -> write("\nInvalid input. Please enter a number between 1 and 4.")
        }
    }
}