package data.mongodb_data.datasource

import org.bson.conversions.Bson

interface DataSource<T> {
    suspend fun add(data: T)
    suspend fun update(data: T)
    suspend fun delete(id: Bson)
    suspend fun getAll(): List<T>
}