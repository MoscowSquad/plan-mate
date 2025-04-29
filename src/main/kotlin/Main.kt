import di.appModule
import di.presentationModule
import di.useCaseModule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import java.util.UUID


fun main() {
    startKoin {
        modules(appModule, useCaseModule, presentationModule)
    }

    // Call the UI

    println(UUID.randomUUID())
}