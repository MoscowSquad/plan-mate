import di.mongodbDataSourceModule
import di.mongodbRepositoryModule
import di.presentationModule
import di.useCaseModule
import kotlinx.datetime.LocalDateTime
import logic.models.AuditLog
import logic.models.AuditType
import logic.usecases.audit.AddAuditLogUseCase
import logic.usecases.audit.ViewAuditLogsByTaskUseCase
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import java.util.*


fun main() {
    val koinApp = startKoin {
        modules(
            mongodbDataSourceModule,
            mongodbRepositoryModule,
            useCaseModule,
            presentationModule
        )
    }

    val taskId = UUID.randomUUID()

    AddAuditLogUseCase(koinApp.koin.get()).invoke(
        log = AuditLog(
            id = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            action = "User logged in",
            timestamp = LocalDateTime(2023, 10, 1, 12, 0),
            auditType = AuditType.TASK,
            entityId = taskId
        )
    )
    print(
        ViewAuditLogsByTaskUseCase(koinApp.koin.get()).invoke(
            taskId = taskId
        )[0]
    )

//    koinApp.koin.get<SessionManager>().setCurrentUser(UserRole.ADMIN)
//    koinApp.koin.get<PlanMateConsoleUI>().start()

    stopKoin()
}