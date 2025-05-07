import data.mongodb_data.datasource.AuditLogDataSource
import data.mongodb_data.dto.AuditLogDto
import di.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.mp.KoinPlatform.getKoin


fun main() {
    startKoin {
        modules(
            mongoModule,
            dataSourceModule,
            repositoryModule,
            useCaseModule,
            presentationModule
        )
    }

    val dataSource: AuditLogDataSource = getKoin().get()

    val deferred = CoroutineScope(Dispatchers.IO).async {
        dataSource.add(
            AuditLogDto(
                id = "123456",
                userId = "userId",
                entityId = "entityId",
                action = "action",
                auditType = "",
                timestamp = "timestamp",
            )
        )
        dataSource.getAll()
    }

    runBlocking {
        print(deferred.await())
    }

//    val mainUi: PlanMateConsoleUI = getKoin().get()
//    mainUi.start()

    stopKoin()
}