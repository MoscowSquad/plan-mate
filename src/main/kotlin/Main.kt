import di.mongodbDataSourceModule
import di.mongodbRepositoryModule
import di.presentationModule
import di.useCaseModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import presentation.PlanMateConsoleUI


@OptIn(DelicateCoroutinesApi::class)
fun main() {
    val koinApp = startKoin {
        modules(
            mongodbDataSourceModule,
            mongodbRepositoryModule,
            useCaseModule,
            presentationModule
        )
    }

    // Launch a top-level coroutine scope without blocking the thread
    CoroutineScope(Dispatchers.Default).launch {
        koinApp.koin.get<PlanMateConsoleUI>().start()
    }

    // Keep the program running (optional: listen for input to exit)
    Thread.currentThread().join()

    stopKoin()
}