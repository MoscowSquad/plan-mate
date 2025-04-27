import di.appModule
import di.presentationModule
import di.useCaseModule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin


fun main() {
    startKoin {
        modules(appModule, useCaseModule, presentationModule)
    }

    // Call the UI

    stopKoin()
}