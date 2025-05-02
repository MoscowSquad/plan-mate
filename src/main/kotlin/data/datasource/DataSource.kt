package data.datasource

import logic.models.*

interface DataSource<T> {
    fun fetch(): List<T>
    fun save(data: List<T>)
}