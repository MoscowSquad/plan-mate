package di

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.mongodb_data.datasource.AuditLogDataSource
import data.mongodb_data.datasource.AuditLogDataSourceImpl
import data.mongodb_data.dto.AuditLogDto
import data.mongodb_data.util.Constants
import org.koin.dsl.module

val mongoModule = module {

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
        database.getCollection<AuditLogDto>("audit_log")
    }

    single<AuditLogDataSource> { AuditLogDataSourceImpl(get()) }


}