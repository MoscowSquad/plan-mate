package di

import data.csv_data.repositories.*
import data.mongodb_data.repositories.AuditLogRepositoryImpl
import data.mongodb_data.repositories.TaskRepositoryImpl
import logic.repositories.*
import org.koin.dsl.module

val repositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get()) }
    single<TasksRepository> { TasksRepositoryImpl(get()) }
    single<TaskStateRepository> { TaskStateRepositoryImpl(get()) }
    single<ProjectsRepository> { ProjectsRepositoryImpl(get()) }
    single<AuditRepository> { AuditRepositoryImpl(get()) }

    single<AuditRepository> { AuditLogRepositoryImpl(get()) }
    single<TasksRepository> { TaskRepositoryImpl(get()) }
}