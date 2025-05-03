package di

import data.repositories.*
import logic.repositories.*
import org.koin.dsl.module

val repositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get()) }
    single<TasksRepository> { TasksRepositoryImpl(get()) }
    single<TaskStateRepository> { TaskStateRepositoryImpl(get()) }
    single<ProjectsRepository> { ProjectsRepositoryImpl(get()) }
    single<AuditRepository> { AuditRepositoryImpl(get()) }
}