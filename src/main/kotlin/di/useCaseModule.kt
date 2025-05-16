package di

import domain.usecases.audit.ViewAuditLogsByProjectUseCase
import domain.usecases.audit.ViewAuditLogsByTaskUseCase
import domain.usecases.auth.LoginUseCase
import domain.usecases.auth.RegisterUseCase
import domain.usecases.project.*
import domain.usecases.task.*
import domain.usecases.task_state.*
import domain.usecases.user.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    // Audit log-related use cases
    singleOf(::ViewAuditLogsByProjectUseCase)
    singleOf(::ViewAuditLogsByTaskUseCase)

    // Authentication-related use cases
    singleOf(::LoginUseCase)
    singleOf(::RegisterUseCase)

    // Project-related use cases
    singleOf(::CreateProjectUseCase)
    singleOf(::DeleteProjectUseCase)
    singleOf(::GetAllProjectsUseCase)
    singleOf(::GetProjectByIdUseCase)
    singleOf(::UpdateProjectUseCase)

    // Task-related use cases
    singleOf(::AddTaskUseCase)
    singleOf(::DeleteTaskUseCase)
    singleOf(::EditTaskUseCase)
    singleOf(::GetTaskByProjectIdUseCase)
    singleOf(::GetTaskByIdUseCase)

    // Task state-related use cases
    singleOf(::AddTaskStateUseCase)
    singleOf(::DeleteTaskStateUseCase)
    singleOf(::EditTaskStateUseCase)
    singleOf(::GetTaskStateByIdUseCase)
    singleOf(::GetTaskStatesByProjectIdUseCase)

    // User-related use cases
    singleOf(::AssignProjectToUserUseCase)
    singleOf(::CreateUserUseCase)
    singleOf(::DeleteUserUseCase)
    singleOf(::GetAllUsersUseCase)
    singleOf(::GetUserByIdUseCase)
    singleOf(::RemoveFromProjectUserUseCase)
}