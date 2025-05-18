package di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import presentation.PlanMateConsoleUI
import presentation.audit.AuditUI
import presentation.audit.ViewAuditLogsByProjectUI
import presentation.audit.ViewAuditLogsByTaskUI
import presentation.auth.AuthenticationUI
import presentation.auth.LoginUserUI
import presentation.auth.RegisterAdminUI
import presentation.io.ConsoleIO
import presentation.io.ConsoleIOImpl
import presentation.project.*
import presentation.state.*
import presentation.sub_task.*
import presentation.task.*
import presentation.user.*
import java.util.*

val presentationModule = module {
    single { Scanner(System.`in`) }
    single<ConsoleIO> { ConsoleIOImpl(get()) }

    // Authentication UI
    singleOf(::LoginUserUI)
    factory {
        RegisterAdminUI(
            registerUseCase = get(),
            consoleIO = get()
        )
    }
    factoryOf(::AuthenticationUI)

    // Project UI
    factoryOf(::CreateProjectUI)
    factoryOf(::DeleteProjectUI)
    factoryOf(::UpdateProjectNameUI)
    factoryOf(::GetAllProjectsUI)
    factoryOf(::ProjectsUI)

    // Task UI
    factoryOf(::CreateTaskUI)
    factoryOf(::DeleteTaskUI)
    factoryOf(::GetAllTasksUI)
    factoryOf(::EditTaskUI)
    factoryOf(::TasksUI)

    // State UI
    factoryOf(::CreateStateUI)
    factoryOf(::DeleteStateUI)
    factoryOf(::GetAllStatesUI)
    factoryOf(::EditStateUI)
    factoryOf(::StateUI)

    //Subtask UI
    factory {
        CreateSubTaskUI(
            createSubTaskUseCase = get(),
            consoleIO = get()
        )
    }
    factory {
        GetSubTasksByTaskIdUI(
            getSubTasksByTaskIdUseCase = get(),
            consoleIO = get()
        )
    }
    factory{
        DeleteSubTaskUI(
            deleteSubTaskUseCase = get(),
            consoleIO = get()
        )
    }
    factory {
        EditSubTaskUI(
            updateSubTaskUseCase = get(),
            consoleIO = get()
        )
    }

    factory{
        SubTaskUI(
            getProjectByIdUseCase = get(),
            createSubTaskUI = get(),
            deleteSubTaskUI = get(),
            getAllTasksUI = get(),
            editTaskUI = get(),
            getAllProjectsUI = get(),
            consoleIO = get(),
            getSubTasksByTaskIdUI = get()
        )
    }

    factory{
        CalculateSubTaskPercentageUI(
            getSubTaskPercentageUseCase = get(),
            consoleIO = get()
        )
    }


    factory {
        CreateUserUI(
            createUserUseCase = get(),
            consoleIO = get()
        )
    }

    factory {
        GetAllUserUI(
            getAllUsersUseCase = get(),
            consoleIO = get()
        )
    }

    factory {
        AssignProjectToUserUI(
            assignProjectToUserUseCase = get(),
            consoleIO = get(),
            getAllProjectsUI = get()
        )
    }

    factory {
        DeleteUserUI(
            deleteUserUseCase = get(),
            consoleIO = get()
        )
    }
    factory {
        GetUserByIdUI(
            getUserByIdUseCase = get(),
            consoleIO = get(),
            getTaskByIdUseCase = get()
        )
    }

    factory {
        UserUI(
            createUserUI = get(),
            getAllUserUI = get(),
            assignProjectToUserUI = get(),
            deleteUserUI = get(),
            consoleIO = get(),
            getUserByIdUI = get()
        )
    }

    factoryOf(::ViewAuditLogsByProjectUI)
    factoryOf(::ViewAuditLogsByTaskUI)
    factoryOf(::AuditUI)
    factoryOf(::PlanMateConsoleUI)
    factoryOf(::GetUserByIdUI)
}