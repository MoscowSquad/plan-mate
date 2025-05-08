package presentation.project

import data.mongodb_data.mappers.toUUID
import logic.models.*
import logic.usecases.project.GetProjectByIdUseCase
import logic.usecases.project.UpdateProjectUseCase
import logic.usecases.task.*
import logic.usecases.task_state.*
import logic.usecases.user.*
import presentation.io.ConsoleIO
import presentation.session.SessionManager
import java.util.*

class UpdateProjectUI(
    private val updateProjectUseCase: UpdateProjectUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val editTaskUseCase: EditTaskUseCase,
    private val getTaskByProjectIdUseCase: GetTaskByProjectIdUseCase,
    private val addTaskStateUseCase: AddTaskStateUseCase,
    private val getTaskStateByProjectIdUseCase: GetTaskStatesByProjectIdUseCase,
    private val deleteTaskStateUseCase: DeleteTaskStateUseCase,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val assignProjectToSpecificUser: AssignProjectToUserUseCase,
    private val getAllUserUseCase: GetAllUsersUseCase,
    private val removeUserFromProject: RemoveFromProjectUserUseCase,
    private val consoleIO: ConsoleIO,
) : ConsoleIO by consoleIO {

    operator fun invoke() {
        consoleIO.write("Enter the project ID to update:")
        val projectId = readProjectId() ?: return

        while (true) {
            consoleIO.write(
                """
                Please select the type of operation:
                1️⃣ - Update project name 
                2️⃣ - Add new task 
                3️⃣ - View all tasks 
                4️⃣ - Edit task 
                5️⃣ - View task states 
                6️⃣ - Add task state 
                7️⃣ - Delete task 
                8️⃣ - Delete task state
                9️⃣ - Assign user to project 
                🔟 - Remove user from project
                1️⃣1️⃣ - Back to previous menu
            """.trimIndent()
            )

            when (consoleIO.read().trim()) {
                "1" -> updateProjectName(projectId)
                "2" -> addNewTask(projectId)
                "3" -> viewAllTasks(projectId)
                "4" -> editTask(projectId)
                "5" -> viewTaskStates(projectId)
                "6" -> addTaskState(projectId)
                "7" -> deleteTask(projectId)
                "8" -> deleteTaskState(projectId)
                "9" -> assignProjectToUser(projectId)
                "10" -> removeUserFromProject(projectId)
                "11" -> return
                else -> consoleIO.write("❌ Please choose a valid option")
            }
        }
    }

    private fun readProjectId(): UUID? {
        return runCatching {
            consoleIO.read().trim().toUUID()
        }.getOrElse {
            consoleIO.write("❌ Invalid project ID format")
            null
        }
    }

    private fun updateProjectName(projectId: UUID) {
        consoleIO.write("Enter the new project name:")
        val newName = consoleIO.read()

        runCatching {
            updateProjectUseCase(
                id = projectId,
                name = newName,
                isAdmin = SessionManager.currentUser?.role == UserRole.ADMIN
            )
        }.onSuccess {
            consoleIO.write("✅ Project updated successfully")
        }.onFailure {
            consoleIO.write("❌ Error updating project: ${it.message}")
        }
    }

    private fun addNewTask(projectId: UUID) {
        consoleIO.write("Enter task name:")
        val name = consoleIO.read()

        consoleIO.write("Enter task description:")
        val description = consoleIO.read()

        // Get available states and let user select one
        val states = getTaskStateByProjectIdUseCase(projectId)
        if (states.isEmpty()) {
            consoleIO.write("❌ No task states available. Please create one first.")
            return
        }

        consoleIO.write("Available task states:")
        states.forEachIndexed { index, state ->
            consoleIO.write("${index + 1}. ${state.name} (ID: ${state.id})")
        }

        consoleIO.write("Select task state (number):")
        val stateIndex = consoleIO.read().toIntOrNull()?.minus(1)
        val stateId = stateIndex?.let { states.getOrNull(it)?.id } ?: run {
            consoleIO.write("❌ Invalid selection")
            return
        }

        val newTask = Task(
            id = UUID.randomUUID(),
            name = name,
            description = description,
            projectId = projectId,
            stateId = stateId
        )

        runCatching {
            addTaskUseCase(newTask)
            consoleIO.write("✅ Task added successfully")
        }.onFailure {
            consoleIO.write("❌ Error adding task: ${it.message}")
        }
    }

    private fun viewAllTasks(projectId: UUID) {
        val tasks = getTaskByProjectIdUseCase(projectId)

        if (tasks.isEmpty()) {
            consoleIO.write("ℹ️ No tasks found for this project")
            return
        }

        consoleIO.write("Tasks in project:")
        tasks.forEach { task ->
            consoleIO.write(
                """
                ------------------------------
                Name: ${task.name}
                Description: ${task.description}
                ID: ${task.id}
                State ID: ${task.stateId}
                ------------------------------
                """.trimIndent()
            )
        }
    }

    private fun editTask(projectId: UUID) {
        viewAllTasks(projectId)
        consoleIO.write("Enter task ID to edit:")
        val taskId = readProjectId() ?: return

        consoleIO.write("Enter new task name (leave empty to keep current):")
        val name = consoleIO.read().takeIf { it.isNotBlank() }

        consoleIO.write("Enter new description (leave empty to keep current):")
        val description = consoleIO.read().takeIf { it.isNotBlank() }

        // Get current task to preserve unchanged values
        val currentTask = getTaskByProjectIdUseCase(projectId)
            .firstOrNull { it.id == taskId } ?: run {
            consoleIO.write("❌ Task not found")
            return
        }

        val updatedTask = currentTask.copy(
            name = name ?: currentTask.name,
            description = description ?: currentTask.description
        )

        runCatching {
            editTaskUseCase(updatedTask)
            consoleIO.write("✅ Task updated successfully")
        }.onFailure {
            consoleIO.write("❌ Error updating task: ${it.message}")
        }
    }

    private fun viewTaskStates(projectId: UUID) {
        val states = getTaskStateByProjectIdUseCase(projectId)

        if (states.isEmpty()) {
            consoleIO.write("ℹ️ No task states defined for this project")
            return
        }

        consoleIO.write("Task states:")
        states.forEach { state ->
            consoleIO.write(
                """
                ------------------------------
                Name: ${state.name}
                ID: ${state.id}
                ------------------------------
                """.trimIndent()
            )
        }
    }

    private fun addTaskState(projectId: UUID) {
        consoleIO.write("Enter new task state name:")
        val name = consoleIO.read()

        val newState = TaskState(
            id = UUID.randomUUID(),
            name = name,
            projectId = projectId
        )

        runCatching {
            addTaskStateUseCase(newState)
            consoleIO.write("✅ Task state added successfully")
        }.onFailure {
            consoleIO.write("❌ Error adding task state: ${it.message}")
        }
    }

    private fun deleteTask(projectId: UUID) {
        viewAllTasks(projectId)
        consoleIO.write("Enter task ID to delete:")
        val taskId = readProjectId() ?: return

        runCatching {
            deleteTaskUseCase(taskId)
            consoleIO.write("✅ Task deleted successfully")
        }.onFailure {
            consoleIO.write("❌ Error deleting task: ${it.message}")
        }
    }

    private fun deleteTaskState(projectId: UUID) {
        viewTaskStates(projectId)
        consoleIO.write("Enter task state ID to delete:")
        val stateId = readProjectId() ?: return

        runCatching {
            deleteTaskStateUseCase(stateId, projectId)
            consoleIO.write("✅ Task state deleted successfully")
        }.onFailure {
            consoleIO.write("❌ Error deleting task state: ${it.message}")
        }
    }

    private fun assignProjectToUser(projectId: UUID) {
        showUsers(UserRole.ADMIN)
        consoleIO.write("Enter user ID to assign:")
        val userId = readProjectId() ?: return

        runCatching {
            assignProjectToSpecificUser(UserRole.MATE, projectId, userId)
            consoleIO.write("✅ User assigned to project successfully")
        }.onFailure {
            consoleIO.write("❌ Error assigning user: ${it.message}")
        }
    }

    private fun removeUserFromProject(projectId: UUID) {
        showUsers(UserRole.ADMIN)
        consoleIO.write("Enter user ID to remove:")
        val userId = readProjectId() ?: return

        runCatching {
            removeUserFromProject(UserRole.MATE, projectId, userId)
            consoleIO.write("✅ User removed from project successfully")
        }.onFailure {
            consoleIO.write("❌ Error removing user: ${it.message}")
        }
    }

    private fun showUsers(role: UserRole) {
        val users = getAllUserUseCase(role)

        if (users.isEmpty()) {
            consoleIO.write("ℹ️ No users found")
            return
        }

        consoleIO.write("Available users:")
        users.forEach { user ->
            consoleIO.write(
                """
                ------------------------------
                Name: ${user.name}
                ID: ${user.id}
                Role: ${user.role}
                ------------------------------
                """.trimIndent()
            )
        }
    }
}