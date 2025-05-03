import di.*
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin


fun main() {
    startKoin {
        modules(dataSourceModule, repositoryModule, useCaseModule, presentationModule)
    }


    stopKoin()
}