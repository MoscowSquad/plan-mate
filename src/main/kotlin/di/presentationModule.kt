package di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import presentation.PlanMateConsoleUI
import presentation.audit.AddAuditLogUI
import presentation.audit.AuditUI
import presentation.audit.ViewAuditLogsByProjectUI
import presentation.audit.ViewAuditLogsByTaskUI
import presentation.auth.LoginUserUI
import presentation.auth.RegisterAdminUI
import presentation.io.ConsoleIO
import presentation.io.ConsoleIOImpl
import java.util.*
import presentation.auth.AuthenticationUI
import presentation.project.*
import presentation.user.*

val presentationModule = module {
    single { Scanner(System.`in`) }
    single { SessionManager() }
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
    factoryOf(::UpdateProjectUI)
    factoryOf(::GetAllProjectsUI)
    factoryOf(::ProjectsUI)

    factory {
        val role = get<SessionManager>().getCurrentUserRole()
            ?: throw IllegalStateException("User must be logged in with a valid role")
        CreateUserUI(
            createUserUseCase = get(),
            currentUserRole = role,
            consoleIO = get()
        )
    }

    factory {
        GetAllUserUI(
            getAllUsersUseCase = get(),
            currentUserRole = { get<SessionManager>().getCurrentUserRole()
                ?: error("No user role set") },
            consoleIO = get()
        )
    }

    factory {
        AssignProjectToUserUI(
            assignProjectToUserUseCase = get(),
            sessionManager = get(),
            consoleIO = get()
        )
    }

    factory {
        GetAllUserUI(
            getAllUsersUseCase = get(),
            currentUserRole = { get<SessionManager>().getCurrentUserRole()
                ?: error("No user role set") },  // This is a Function0 type
            consoleIO = get()
        )
    }

    factory {
        DeleteUserUI(
            deleteUserUseCase = get(),
            currentUserRole = { get<SessionManager>().getCurrentUserRole()
                ?: error("No user role set") },
            consoleIO = get()
        )
    }
    factory {
        GetUserByIdUI(
            getUserByIdUseCase = get(),
            consoleIO = get()
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

    factoryOf(::AddAuditLogUI)
    factoryOf(::ViewAuditLogsByProjectUI)
    factoryOf(::ViewAuditLogsByTaskUI)
    factoryOf(::AuditUI)
    factoryOf(::PlanMateConsoleUI)
    factoryOf(::GetUserByIdUI)
}