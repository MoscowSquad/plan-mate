package di

import data.csv_data.repositories.*
import logic.repositories.*
import org.koin.dsl.module

val csvRepositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get()) }
    single<TasksRepository> { TasksRepositoryImpl(get()) }
    single<TaskStateRepository> { TaskStateRepositoryImpl(get()) }
    single<ProjectsRepository> { ProjectsRepositoryImpl(get()) }
    single<AuditRepository> { AuditRepositoryImpl(get()) }
}