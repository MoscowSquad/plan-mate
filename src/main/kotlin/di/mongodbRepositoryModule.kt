package di

import data.mongodb_data.repositories.*
import domain.repositories.*
import org.koin.dsl.module

val mongodbRepositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<TasksRepository> { TaskRepositoryImpl(get(), get()) }
    single<ProjectsRepository> { ProjectsRepositoryImpl(get(), get(), get()) }
    single<AuditRepository> { AuditLogRepositoryImpl(get()) }
    single<TaskStateRepository> { TaskStateRepositoryImpl(get(), get()) }
    single<AuthenticationRepository> { UserRepositoryImpl(get(), get()) }
}