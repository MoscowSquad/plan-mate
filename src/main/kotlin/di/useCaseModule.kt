package di

import logic.usecases.audit.AddAuditLogUseCase
import logic.usecases.audit.ViewAuditLogsByProjectUseCase
import logic.usecases.audit.ViewAuditLogsByTaskUseCase
import logic.usecases.auth.LoginUseCase
import logic.usecases.auth.RegisterUseCase
import logic.usecases.project.*
import logic.usecases.task.*
import logic.usecases.task_state.*
import logic.usecases.user.*
import org.koin.dsl.module

val useCaseModule = module {
    // Audit log-related use cases
    single { AddAuditLogUseCase(get()) }
    single { ViewAuditLogsByProjectUseCase(get()) }
    single { ViewAuditLogsByTaskUseCase(get()) }

    // Authentication-related use cases
    single { LoginUseCase(get()) }
    single { RegisterUseCase(get()) }

    // Project-related use cases
    single { CreateProjectUseCase(get()) }
    single { DeleteProjectUseCase(get()) }
    single { GetAllProjectsUseCase(get()) }
    single { GetProjectByIdUseCase(get()) }
    single { UpdateProjectUseCase(get()) }

    // Task-related use cases
    single { AddTaskUseCase(get()) }
    single { DeleteTaskUseCase(get()) }
    single { EditTaskUseCase(get()) }
    single { GetTaskByProjectIdUseCase(get()) }
    single { GetTaskByIdUseCase(get()) }

    // Task state-related use cases
    single { AddTaskStateUseCase(get()) }
    single { DeleteTaskStateUseCase(get()) }
    single { EditTaskStateUseCase(get()) }
    single { GetTaskStateByIdUseCase(get()) }
    single { GetTaskStatesByProjectIdUseCase(get()) }

    // User-related use cases
    single { AssignProjectToUserUseCase(get()) }
    single { CreateUserUseCase(get()) }
    single { DeleteUserUseCase(get()) }
    single { GetAllUsersUseCase(get()) }
    single { GetUserByIdUseCase(get()) }
    single { RemoveFromProjectUserUseCase(get()) }
}