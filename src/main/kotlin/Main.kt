import di.mongodbDataSourceModule
import di.mongodbRepositoryModule
import di.presentationModule
import di.useCaseModule
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

    koinApp.koin.get<PlanMateConsoleUI>().start()

    stopKoin()
}