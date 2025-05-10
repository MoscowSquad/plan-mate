package presentation.project

import io.mockk.*
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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import presentation.session.LoggedInUser
import presentation.session.SessionManager
import java.util.*

class UpdateProjectNameUITest {
    private lateinit var updateProjectUseCase: UpdateProjectUseCase
    private lateinit var addTaskUseCase: AddTaskUseCase
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private lateinit var editTaskUseCase: EditTaskUseCase
    private lateinit var getTaskByProjectIdUseCase: GetTaskByProjectIdUseCase
    private lateinit var addTaskStateUseCase: AddTaskStateUseCase
    private lateinit var getTaskStateByProjectIdUseCase: GetTaskStatesByProjectIdUseCase
    private lateinit var deleteTaskStateUseCase: DeleteTaskStateUseCase
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase
    private lateinit var assignProjectToSpecificUser: AssignProjectToUserUseCase
    private lateinit var getAllUserUseCase: GetAllUsersUseCase
    private lateinit var removeUserFromProject: RemoveFromProjectUserUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var updateProjectNameUI: UpdateProjectNameUI

    @BeforeEach
    fun setUp() {
        updateProjectUseCase = mockk(relaxed = true)
        addTaskUseCase = mockk(relaxed = true)
        deleteTaskUseCase = mockk(relaxed = true)
        editTaskUseCase = mockk(relaxed = true)
        getTaskByProjectIdUseCase = mockk(relaxed = true)
        addTaskStateUseCase = mockk(relaxed = true)
        getTaskStateByProjectIdUseCase = mockk(relaxed = true)
        deleteTaskStateUseCase = mockk(relaxed = true)
        getProjectByIdUseCase = mockk(relaxed = true)
        assignProjectToSpecificUser = mockk(relaxed = true)
        getAllUserUseCase = mockk(relaxed = true)
        removeUserFromProject = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)

        updateProjectNameUI = UpdateProjectNameUI(
            updateProjectUseCase,
            addTaskUseCase,
            deleteTaskUseCase,
            editTaskUseCase,
            getTaskByProjectIdUseCase,
            addTaskStateUseCase,
            getTaskStateByProjectIdUseCase,
            deleteTaskStateUseCase,
            getProjectByIdUseCase,
            assignProjectToSpecificUser,
            getAllUserUseCase,
            removeUserFromProject,
            consoleIO
        )

        mockkObject(SessionManager)
        SessionManager.currentUser = null
    }

    @Test
    fun `should update project name successfully when valid inputs and regular user`() {
        val projectId = UUID.randomUUID()
        val projectName = "Updated Project Name"
        SessionManager.currentUser = LoggedInUser(UUID.randomUUID(), "user", UserRole.MATE, listOf())

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), "1", projectName)
        every { updateProjectUseCase(projectId, projectName, false) } returns true

        updateProjectNameUI()

        verifySequence {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("Please Enter the new project name")
            consoleIO.read()
            updateProjectUseCase(projectId, projectName, false)
            consoleIO.write("Project updated successfully.")
        }
    }

    @Test
    fun `should update project name successfully when valid inputs and admin user`() {
        val projectId = UUID.randomUUID()
        val projectName = "Updated Project Name"
        SessionManager.currentUser = LoggedInUser(UUID.randomUUID(), "admin", UserRole.ADMIN, listOf())

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), "1", projectName)
        every { updateProjectUseCase(projectId, projectName, true) } returns true

        updateProjectNameUI()

        verifySequence {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("Please Enter the new project name")
            consoleIO.read()
            updateProjectUseCase(projectId, projectName, true)
            consoleIO.write("Project updated successfully.")
        }
    }

    @Test
    fun `should handle error when update project name fails`() {
        val projectId = UUID.randomUUID()
        val projectName = "Updated Project Name"
        val errorMessage = "Project not found"
        SessionManager.currentUser = LoggedInUser(UUID.randomUUID(), "user", UserRole.MATE, listOf())

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), "1", projectName)
        every { updateProjectUseCase(projectId, projectName, false) } throws IllegalArgumentException(errorMessage)

        updateProjectNameUI()

        verifySequence {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("Please Enter the new project name")
            consoleIO.read()
            updateProjectUseCase(projectId, projectName, false)
            consoleIO.write("Error updating project: $errorMessage")
        }
    }

    // Task Tests
    @Test
    fun `should add new task successfully`() {
        val projectId = UUID.randomUUID()
        val taskName = "New Task"
        val taskDescription = "Task Description"
        val project = mockk<Project>(relaxed = true)

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), "2", taskName, taskDescription)
        every { getProjectByIdUseCase(projectId) } returns project
        every { addTaskUseCase(any()) } returns true

        updateProjectNameUI()

        verify {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("please enter the name of task")
            consoleIO.read()
            consoleIO.write("please enter the description of task")
            consoleIO.read()
            getProjectByIdUseCase(projectId)
            addTaskUseCase(any())
            consoleIO.write(match { it.contains("✅ Task Added") })
        }
    }

    @Test
    fun `should get all tasks by project ID successfully`() {
        val projectId = UUID.randomUUID()
        val tasks = listOf(
            Task(UUID.randomUUID(), "Task 1", "Description 1", projectId, UUID.randomUUID()),
            Task(UUID.randomUUID(), "Task 2", "Description 2", projectId, UUID.randomUUID())
        )

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), "3")
        every { getTaskByProjectIdUseCase(projectId) } returns tasks

        updateProjectNameUI()

        verify {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write(any())
            consoleIO.read()
            getTaskByProjectIdUseCase(projectId)
            consoleIO.write(match { it.contains("task name") })
            consoleIO.write(match { it.contains("task name") })
        }
    }

    @Test
    fun `should edit task successfully`() {
        val projectId = UUID.randomUUID()
        val taskId = UUID.randomUUID()
        val stateId = UUID.randomUUID()
        val taskName = "Updated Task"
        val taskDescription = "Updated Description"

        every { consoleIO.read() } returnsMany listOf(
            projectId.toString(), "4", taskId.toString(), stateId.toString(), taskName, taskDescription
        )
        every { editTaskUseCase(any()) } returns true

        updateProjectNameUI()

        verify {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("please enter task id")
            consoleIO.read()
            consoleIO.write("please enter state id")
            consoleIO.read()
            consoleIO.write("please enter the name of task")
            consoleIO.read()
            consoleIO.write("please enter the description of task")
            consoleIO.read()
            editTaskUseCase(any())
            consoleIO.write("✅ task edited successfully")
        }
    }

    @Test
    fun `should get task states successfully`() {
        val projectId = UUID.randomUUID()
        val taskStates = listOf(
            TaskState(UUID.randomUUID(), "To Do", projectId),
            TaskState(UUID.randomUUID(), "In Progress", projectId)
        )

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), "5")
        every { getTaskStateByProjectIdUseCase(projectId) } returns taskStates

        updateProjectNameUI()

        verify {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write(any())
            consoleIO.read()
            getTaskStateByProjectIdUseCase(projectId)
            consoleIO.write(match { it.contains(taskStates[0].toString()) })
            consoleIO.write(match { it.contains(taskStates[1].toString()) })
        }
    }

    @Test
    fun `should add task state successfully`() {
        val projectId = UUID.randomUUID()
        val stateName = "New State"

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), "6", stateName)
        every { addTaskStateUseCase(any()) } returns true

        updateProjectNameUI()

        verify {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("please enter the name of State")
            consoleIO.read()
            addTaskStateUseCase(any())
            consoleIO.write("✅ correctly updated")
        }
    }

    @Test
    fun `should delete task successfully`() {
        val taskId = UUID.randomUUID()

        every { consoleIO.read() } returnsMany listOf(UUID.randomUUID().toString(), "7", taskId.toString())
        every { deleteTaskUseCase(taskId) } returns true

        updateProjectNameUI()

        verify {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("please enter task ID ")
            consoleIO.read()
            deleteTaskUseCase(taskId)
            consoleIO.write("✅ correctly deleted ")
        }
    }

    @Test
    fun `should delete task state successfully`() {
        val projectId = UUID.randomUUID()
        val stateId = UUID.randomUUID()

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), "8", stateId.toString())
        every { deleteTaskStateUseCase(stateId, projectId) } returns true

        updateProjectNameUI()

        verify {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("please enter task state id ")
            consoleIO.read()
            deleteTaskStateUseCase(stateId, projectId)
            consoleIO.write("✅ correctly deleted state")
        }
    }

    @Test
    fun `should assign user to project successfully`() {
        val projectId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val users = listOf(User(userId, "test", "pass", UserRole.ADMIN, listOf()))

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), "9", userId.toString())
        every { getAllUserUseCase(UserRole.ADMIN) } returns users
        every { assignProjectToSpecificUser(UserRole.MATE, projectId, userId) } returns true

        updateProjectNameUI()

        verify {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write(any())
            consoleIO.read()
            getAllUserUseCase(UserRole.ADMIN)
            consoleIO.write(match { it.contains("username") })
            consoleIO.write("please user the user id ")
            consoleIO.read()
            assignProjectToSpecificUser(UserRole.MATE, projectId, userId)
            consoleIO.write("✅ Assigned successfully")
        }
    }

    @Test
    fun `should remove user from project successfully`() {
        val projectId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val users = listOf(User(userId, "test", "pass", UserRole.ADMIN, listOf()))

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), "10", userId.toString())
        every { getAllUserUseCase(UserRole.ADMIN) } returns users
        every { removeUserFromProject(UserRole.MATE, projectId, userId) } returns true

        updateProjectNameUI()

        verify {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write(any())
            consoleIO.read()
            getAllUserUseCase(UserRole.ADMIN)
            consoleIO.write(match { it.contains("username") })
            consoleIO.write("please user the user id ")
            consoleIO.read()
            removeUserFromProject(UserRole.MATE, projectId, userId)
            consoleIO.write("✅ removed successfully")
        }
    }

    @Test
    fun `should handle invalid menu option`() {
        val projectId = UUID.randomUUID()

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), "999", "11")

        updateProjectNameUI()

        verify {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("❌please choose correct option")
        }
    }

    @Test
    fun `should handle error when adding new task fails`() {
        val projectId = UUID.randomUUID()
        val taskName = "New Task"
        val taskDescription = "Task Description"
        val errorMessage = "Failed to add task"

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), "2", taskName, taskDescription)
        every { getProjectByIdUseCase(projectId) } throws IllegalArgumentException(errorMessage)

        updateProjectNameUI()

        verify {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("please enter the name of task")
            consoleIO.read()
            consoleIO.write("please enter the description of task")
            consoleIO.read()
            getProjectByIdUseCase(projectId)
            consoleIO.write(match { it.contains("❌ Please enter valid information") })
        }
    }

    @Test
    fun `should handle invalid task ID when deleting task`() {
        val invalidTaskId = "not-a-uuid"

        every { consoleIO.read() } returnsMany listOf(UUID.randomUUID().toString(), "7", invalidTaskId)

        updateProjectNameUI()

        verify {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("please enter task ID ")
            consoleIO.read()
            consoleIO.write("❌ please enter the valid id")
        }
    }

    @Test
    fun `should handle invalid state ID when deleting task state`() {
        val projectId = UUID.randomUUID()
        val invalidStateId = "not-a-uuid"

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), "8", invalidStateId)

        updateProjectNameUI()

        verify {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("please enter task state id ")
            consoleIO.read()
            consoleIO.write("❌ please enter the valid id")
        }
    }

    @Test
    fun `should handle invalid user ID when assigning to project`() {
        val projectId = UUID.randomUUID()
        val invalidUserId = "not-a-uuid"
        val users = listOf(User(UUID.randomUUID(), "test", "pass", UserRole.ADMIN, listOf()))

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), "9", invalidUserId)
        every { getAllUserUseCase(UserRole.ADMIN) } returns users

        updateProjectNameUI()

        verify {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write(any())
            consoleIO.read()
            getAllUserUseCase(UserRole.ADMIN)
            consoleIO.write(match { it.contains("username") })
            consoleIO.write("please user the user id ")
            consoleIO.read()
            consoleIO.write("❌ please enter the valid id")
        }
    }

    @Test
    fun `should handle invalid user ID when removing from project`() {
        val projectId = UUID.randomUUID()
        val invalidUserId = "not-a-uuid"
        val users = listOf(User(UUID.randomUUID(), "test", "pass", UserRole.ADMIN, listOf()))

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), "10", invalidUserId)
        every { getAllUserUseCase(UserRole.ADMIN) } returns users

        updateProjectNameUI()

        verify {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write(any())
            consoleIO.read()
            getAllUserUseCase(UserRole.ADMIN)
            consoleIO.write(match { it.contains("username") })
            consoleIO.write("please user the user id ")
            consoleIO.read()
            consoleIO.write("❌ please enter the valid id")
        }
    }

    @Test
    fun `should exit when option 11 is selected`() {
        val projectId = UUID.randomUUID()

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), "11")

        updateProjectNameUI()

        verify {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write(any())
            consoleIO.read()
        }
    }

    @Test
    fun `should display no users when list is empty`() {
        val projectId = UUID.randomUUID()
        val emptyUsers = emptyList<User>()

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), "9")
        every { getAllUserUseCase(UserRole.ADMIN) } returns emptyUsers

        updateProjectNameUI()

        verify {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write(any())
            consoleIO.read()
            getAllUserUseCase(UserRole.ADMIN)
            consoleIO.write("No Users Exist")
            consoleIO.write("please user the user id ")
        }
    }
}