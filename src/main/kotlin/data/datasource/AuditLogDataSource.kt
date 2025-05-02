package data.datasource

import logic.models.AuditLog
import data.csv_parser.AuditLogCsvParser
import data.csv_parser.CsvHandler

class AuditLogDataSource(
    private val csvParser: AuditLogCsvParser,
) : DataSource<AuditLog> {
    override fun fetch(): List<AuditLog> {
        TODO("Not yet implemented")
    }

    override fun save(data: List<AuditLog>) {
        TODO("Not yet implemented")
    }
}