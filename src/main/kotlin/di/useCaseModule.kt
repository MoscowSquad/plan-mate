package di

import data.repositories.UserRepositoryImpl
import logic.repositories.UserRepository
import org.koin.dsl.module

val useCaseModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
}