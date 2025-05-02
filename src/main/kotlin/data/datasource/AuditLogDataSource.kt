package data.datasource

import logic.models.AuditLog
import data.csv_parser.AuditLogCsvParser
import data.csv_parser.CsvHandler

class AuditLogDataSource(
    private val csvHandler: CsvHandler,
    private val csvParser: AuditLogCsvParser,
) : DataSource<AuditLog> {
    override fun fetch(): List<AuditLog> {
        val lines = csvHandler.getLines()
        return csvParser.parse(lines)
    }

    override fun save(data: List<AuditLog>) {
        val serializedData = csvParser.serialize(data)
        csvHandler.write(serializedData)
    }
}