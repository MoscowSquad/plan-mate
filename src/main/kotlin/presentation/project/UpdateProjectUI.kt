package presentation.project

import data.mongodb_data.mappers.toUUID
import logic.models.*
import logic.usecases.project.GetProjectByIdUseCase
import logic.usecases.project.UpdateProjectUseCase
import logic.usecases.task.AddTaskUseCase
import logic.usecases.task.DeleteTaskUseCase
import logic.usecases.task.EditTaskUseCase
import logic.usecases.task.GetTaskByProjectIdUseCase
import logic.usecases.task_state.AddTaskStateUseCase
import logic.usecases.task_state.DeleteTaskStateUseCase
import logic.usecases.task_state.GetTaskStatesByProjectIdUseCase
import logic.usecases.user.AssignProjectToUserUseCase
import logic.usecases.user.GetAllUsersUseCase
import logic.usecases.user.RemoveFromProjectUserUseCase
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
        var projectId: UUID? = null
        runCatching {
            projectId = consoleIO.read().trimIndent().toUUID()
        }.onFailure {
            consoleIO.write("❌ please enter correct ID ")
        }
        consoleIO.write(
            """
            please select the type of operation 
            1️⃣- update project name 
            2️⃣- add new task 
            3️⃣- get all tasks 
            4️⃣- edit task 
            5️⃣- get task states 
            6️⃣- add task state 
            7️⃣- delete task 
            8️⃣- delete task state
            9️⃣- assign specific user to project 
            🔟- remove user form project
            1️⃣1️⃣- back to previous list 
        """.trimIndent()
        )

        val selectedProjectOption = consoleIO.read().trimIndent()
        when (selectedProjectOption) {
            "1" -> projectId?.let { updateProjectName(it) }
            "2" -> projectId?.let { addNewTask(projectId = it, stateId = UUID.randomUUID()/*temporary*/) }
            "3" -> projectId?.let { getAllTasksByProjectId(it) }
            "4" -> projectId?.let { editTask(it) }
            "5" -> projectId?.let { getTaskStates(projectId = it) }
            "6" -> projectId?.let { addTaskState(projectId = it) }
            "7" -> projectId?.let { deleteTask(it) }
            "8" -> projectId?.let { deleteTaskState(it) }
            "9" -> projectId?.let { assignProjectToUser(it) }
            "10" -> projectId?.let { removeUserFromProject(it) }
            "11" -> System.gc()
            else -> {
                consoleIO.write("❌please choose correct option")
                invoke()
            }
        }
    }

    private fun updateProjectName(projectId: UUID) {
        consoleIO.write("Please Enter the new project name")
        val newProjectName = consoleIO.read()
        runCatching {
            updateProjectUseCase(
                id = projectId,
                name = newProjectName,
                isAdmin = SessionManager.currentUser?.role == UserRole.ADMIN
            )
        }.onSuccess {
            consoleIO.write("Project updated successfully.")
        }.onFailure { exception ->
            consoleIO.write("Error updating project: ${exception.message}")
        }
    }

    private fun addNewTask(projectId: UUID, stateId: UUID) {
        val taskId = UUID.randomUUID()
        consoleIO.write("please enter the name of task")
        val taskName = consoleIO.read()

        consoleIO.write("please enter the description of task")
        val taskDescription = consoleIO.read()

        var projectName: Project? = null

        val newTask = Task(
            id = taskId,
            name = taskName,
            description = taskDescription,
            projectId = projectId,
            stateId = stateId
        )

        runCatching {
            if (projectName == null)
                projectName = getProjectByIdUseCase(projectId)
            addTaskUseCase(newTask)
        }.onSuccess {
            consoleIO.write("✅ Task Added for $projectName ")
        }.onFailure { error ->
            consoleIO.write("❌ Please enter valid information there are $error")
        }
    }

    private fun getAllTasksByProjectId(projectId: UUID) {
        var tasks: List<Task>? = null
        runCatching {
            tasks = getTaskByProjectIdUseCase(projectId)
            if(tasks!!.isEmpty()) {
                consoleIO.write("No Tasks exist ")
                return
            }
        }.onSuccess {
            tasks?.forEach { task ->
                consoleIO.write(
                    """
                -------------------------------------------------------
                | task name is : ${task.name}                         |      
                | task description is : ${task.description}           |
                | task id : ${task.id}                                |
                -------------------------------------------------------
                    
                """.trimIndent()
                )
            }
        }.onFailure { error ->
            consoleIO.write("❌ $error")
        }
    }

    private fun editTask(projectId: UUID) {
        consoleIO.write("please enter task id")
        val taskId = consoleIO.read().toUUID()

        consoleIO.write("please enter state id")
        val stateId = consoleIO.read().toUUID()

        consoleIO.write("please enter the name of task")
        val taskName = consoleIO.read()

        consoleIO.write("please enter the description of task")
        val taskDescription = consoleIO.read()

        val newTask = Task(
            id = taskId,
            name = taskName,
            description = taskDescription,
            projectId = projectId,
            stateId = stateId
        )

        runCatching {
            editTaskUseCase(newTask)
        }.onSuccess {
            consoleIO.write("✅ task edited successfully")
        }.onFailure { error ->
            consoleIO.write("❌fail : $error")
        }
    }

    private fun getTaskStates(projectId: UUID) {
        var taskStates: List<TaskState>? = null
        runCatching {
            taskStates = getTaskStateByProjectIdUseCase(projectId)
        }.onSuccess {
            taskStates?.forEach { taskState ->
                consoleIO.write("$taskState \n")
            }
        }.onFailure { error ->
            consoleIO.write("❌ $error")
        }
    }

    private fun addTaskState(projectId: UUID) {
        val taskStateId = UUID.randomUUID()

        consoleIO.write("please enter the name of State")
        val nameOfTaskState = consoleIO.read()

        val newTaskState = TaskState(
            id = taskStateId,
            name = nameOfTaskState,
            projectId = projectId
        )

        runCatching {
            addTaskStateUseCase(newTaskState)
        }.onSuccess {
            consoleIO.write("✅ correctly updated")
        }.onFailure { error ->
            consoleIO.write("❌ not added task $error")
        }

    }

    private fun deleteTask(projectId: UUID) {
        getAllTasksByProjectId(projectId)
        consoleIO.write("please enter task ID ")
        var taskId: UUID? = null
        runCatching {
            taskId = consoleIO.read().trimIndent().toUUID()
        }.onFailure {
            consoleIO.write("❌ please enter the valid id")
        }

        runCatching {
            taskId?.let { deleteTaskUseCase(it) }
        }.onSuccess {
            consoleIO.write("✅ correctly deleted ")
        }.onFailure { error ->
            consoleIO.write("❌ fail: $error")
        }
    }

    private fun deleteTaskState(projectId: UUID) {
        getTaskStates(projectId)
        consoleIO.write("please enter task state id ")
        var taskStateId: UUID? = null
        runCatching {
            taskStateId = consoleIO.read().trimIndent().toUUID()
        }.onFailure {
            consoleIO.write("❌ please enter the valid id")
        }

        runCatching {
            taskStateId?.let { deleteTaskStateUseCase(stateId = it, projectId = projectId) }
        }.onSuccess {
            consoleIO.write("✅ correctly deleted state")
        }.onFailure { error ->
            consoleIO.write("❌fail: $error")
        }
    }

    private fun assignProjectToUser(projectId: UUID) {
        showUsers(UserRole.ADMIN)
        var userId: UUID? = null
        consoleIO.write("please user the user id ")
        runCatching {
            userId = consoleIO.read().trimIndent().toUUID()
        }.onFailure {
            consoleIO.write("❌ please enter the valid id")
        }

        runCatching {
            userId?.let { assignProjectToSpecificUser(UserRole.ADMIN, projectId = projectId, userId = it) }
        }.onSuccess {
            consoleIO.write("✅ Assigned successfully")
        }.onFailure { error ->
            consoleIO.write("❌ Assigned Failed $error")
        }
    }

    private fun removeUserFromProject(projectId: UUID) {
        showUsers(UserRole.ADMIN)
        var userId: UUID? = null
        consoleIO.write("please user the user id ")
        runCatching {
            userId = consoleIO.read().trimIndent().toUUID()
        }.onFailure {
            consoleIO.write("❌ please enter the valid id")
        }
        runCatching {
            userId?.let { removeUserFromProject(UserRole.MATE, projectId = projectId, userId = it) }
        }.onSuccess {
            consoleIO.write("✅ removed successfully")
        }.onFailure { error ->
            consoleIO.write("❌ removed Failed $error")
        }
    }

    private fun showUsers(userRole: UserRole) {
        var users: List<User>? = null
        runCatching {
            users = getAllUserUseCase(userRole)
        }.onSuccess {
            if (users?.isEmpty() == true) {
                consoleIO.write("No Users Exist")
            } else {
                users?.forEach { user ->
                    consoleIO.write(
                        """
               |-----------------------------|           
               |   username : ${user.name}   |
               |   id: ${user.id}            |
               |   role : ${user.role}       |
               |-----------------------------|
                """.trimIndent()
                    )
                }
            }

        }.onFailure { error ->
            consoleIO.write("❌ fail: $error ")
        }
    }

}