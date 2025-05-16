import di.mongodbDataSourceModule
import di.mongodbRepositoryModule
import di.presentationModule
import di.useCaseModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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


    CoroutineScope(Dispatchers.Default).launch {
        koinApp.koin.get<PlanMateConsoleUI>().start()
    }

    // Keep the program running
    Thread.currentThread().join()

    stopKoin()
}