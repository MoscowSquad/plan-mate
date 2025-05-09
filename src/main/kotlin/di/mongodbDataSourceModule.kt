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

    single<AuditLogDataSource> {
        val db: MongoDatabase = get()
        val collection = db.getCollection<AuditLogDto>(Constants.AUDIT_LOG_COLLECTION)
        AuditLogDataSourceImpl(collection)
    }

    single<TaskDataSource> {
        val db: MongoDatabase = get()
        val collection = db.getCollection<TaskDto>(Constants.TASK_COLLECTION)
        TaskDataSourceImpl(collection)
    }

    single<ProjectsDataSource> {
        val db: MongoDatabase = get()
        val collection = db.getCollection<ProjectDto>(Constants.PROJECT_COLLECTION)
        ProjectsDataSourceImpl(collection)
    }

    single<UserDataSource> {
        val db: MongoDatabase = get()
        val collection = db.getCollection<UserDto>(Constants.USER_COLLECTION)
        UserDataSourceImpl(collection)
    }

    single<TaskStateDataSource> {
        val db: MongoDatabase = get()
        val collection = db.getCollection<TaskStateDto>(Constants.TASK_STATE_COLLECTION)
        TaskStateDataSourceImpl(collection)
    }

}