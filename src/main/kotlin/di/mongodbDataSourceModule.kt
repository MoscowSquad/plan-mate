package di

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.data_source.*
import data.mongodb_data.datasource.*
import data.mongodb_data.dto.*
import data.mongodb_data.util.Constants
import org.koin.dsl.module

val mongodbDataSourceModule = module {

    single<MongoClient> {
        val connectionString = System.getenv(Constants.MONGODB_URI)
            ?: throw IllegalArgumentException("${Constants.MONGODB_URI} not set in environment variable")
        MongoClient.create(connectionString)
    }

    single<MongoDatabase> {
        val client: MongoClient = get()
        client.getDatabase(Constants.DATABASE_NAME)
    }

    single {
        val database: MongoDatabase = get()
        database.getCollection<AuditLogDto>(Constants.AUDIT_LOG_COLLECTION)
    }

    single {
        val database: MongoDatabase = get()
        database.getCollection<ProjectDto>(Constants.PROJECT_COLLECTION)
    }

    single {
        val database: MongoDatabase = get()
        database.getCollection<TaskDto>(Constants.TASK_COLLECTION)
    }
    single {
        val database: MongoDatabase = get()
        database.getCollection<UserDto>(Constants.USER_COLLECTION)
    }
    single {
        val database: MongoDatabase = get()
        database.getCollection<TaskStateDto>(Constants.TASK_STATE_COLLECTION)
    }
    single {
        val database: MongoDatabase = get()
        database.getCollection<TaskStateDto>(Constants.STATE_COLLECTION)
    }

    single<AuditLogDataSource> { AuditLogDataSourceImpl(get()) }
    single<TaskDataSource> { TaskDataSourceImpl(get()) }
    single<ProjectsDataSource> { ProjectsDataSourceImpl(get()) }
    single<StateDataSource> { StateDataSourceImpl(get()) }
    single<UserDataSource> { UserDataSourceImpl(get()) }
    single<TaskStateDataSource> { TaskStateDataSourceImpl(get()) }

}