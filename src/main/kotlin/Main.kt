import di.appModule
import di.presentationModule
import di.useCaseModule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.java.KoinJavaComponent.getKoin
import org.koin.mp.KoinPlatform
import presentation.PlanMateConsoleUI
import presentation.user.GetAllUserUI


fun main() {
    startKoin {
        modules(appModule, useCaseModule, presentationModule)
    }


    val planMateConsole:PlanMateConsoleUI = KoinPlatform.getKoin().get()

    planMateConsole.start()
    stopKoin()
}