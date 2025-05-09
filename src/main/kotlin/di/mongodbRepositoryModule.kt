package di

import data.mongodb_data.repositories.*
import logic.repositories.*
import org.koin.dsl.module

val mongodbRepositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<TasksRepository> { TaskRepositoryImpl(get()) }
    single<ProjectsRepository> { ProjectsRepositoryImpl(get()) }
    single<AuditRepository> { AuditLogRepositoryImpl(get()) }
    single<TaskStateRepository> { TaskStateRepositoryImpl(get()) }
    single<StateRepository> { StateRepositoryImpl(get()) }
}