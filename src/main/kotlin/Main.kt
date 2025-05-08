import di.*
import logic.models.UserRole
import org.koin.core.context.startKoin
import presentation.PlanMateConsoleUI


fun main() {
    val koinApp = startKoin {
        modules(
            dataSourceModule,
            repositoryModule,
            useCaseModule,
            presentationModule
        )
    }

    koinApp.koin.get<SessionManager>().setCurrentUser(UserRole.ADMIN)

    koinApp.koin.get<PlanMateConsoleUI>().start()
}