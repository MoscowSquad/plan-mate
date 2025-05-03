package di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import presentation.PlanMateConsoleUI
import presentation.auth.LoginUserUI
import presentation.auth.RegisterAdminUI
import presentation.io.ConsoleIO
import presentation.io.ConsoleIOImpl
import java.util.*
import presentation.auth.AuthenticationUI
import presentation.project.*

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
    factoryOf(::UpdateProjectUI)
    factoryOf(::GetAllProjectsUI)
    factoryOf(::ProjectsUI)


    factoryOf(::PlanMateConsoleUI)
}