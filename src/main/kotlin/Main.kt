import di.*
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.mp.KoinPlatform.getKoin
import presentation.PlanMateConsoleUI


fun main() {
    startKoin {
        modules(dataSourceModule, repositoryModule, useCaseModule, presentationModule)
    }

    val mainUi: PlanMateConsoleUI = getKoin().get()
    mainUi.start()

    stopKoin()
}