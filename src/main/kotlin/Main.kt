import di.*
import logic.models.UserRole
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import presentation.PlanMateConsoleUI


fun main() {
    val koinApp = startKoin {
        modules(
            mongodbDataSourceModule,
            mongodbRepositoryModule,
            useCaseModule,
            presentationModule
        )
    }

    koinApp.koin.get<SessionManager>().setCurrentUser(UserRole.ADMIN)
    koinApp.koin.get<PlanMateConsoleUI>().start()

    stopKoin()
}