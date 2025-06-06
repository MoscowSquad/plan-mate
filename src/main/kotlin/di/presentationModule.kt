package di

import org.koin.core.module.dsl.factoryOf
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
    factoryOf(::LoginUserUI)
    factoryOf(::RegisterAdminUI)
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
    factoryOf(::CreateTaskStateUI)
    factoryOf(::DeleteTaskStateUI)
    factoryOf(::GetAllTaskStatesUI)
    factoryOf(::EditTaskStateUI)
    factoryOf(::TaskStateUI)

    //Subtask UI
    factoryOf(::CreateSubTaskUI)
    factoryOf(::GetSubTasksByTaskIdUI)
    factoryOf(::DeleteSubTaskUI)
    factoryOf(::EditSubTaskUI)
    factoryOf(::CreateSubTaskUI)
    factoryOf(::CalculateSubTaskPercentageUI)
    factoryOf(::SubTaskUI)

    // User UI
    factoryOf(::CreateUserUI)
    factoryOf(::GetAllUserUI)
    factoryOf(::AssignProjectToUserUI)
    factoryOf(::DeleteUserUI)
    factoryOf(::GetUserByIdUI)
    factoryOf(::UserUI)

    // Audit UI
    factoryOf(::ViewAuditLogsByProjectUI)
    factoryOf(::ViewAuditLogsByTaskUI)
    factoryOf(::AuditUI)

    // Main UI
    factoryOf(::PlanMateConsoleUI)
}