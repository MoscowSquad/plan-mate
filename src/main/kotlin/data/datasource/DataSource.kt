package data.datasource

interface DataSource<T> {
    fun fetch(): List<T>
    fun save(data: List<T>)
}