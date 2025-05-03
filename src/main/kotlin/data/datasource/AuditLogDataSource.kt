package data.datasource

import data.csv_parser.AuditLogCsvParser
import data.csv_parser.CsvHandler
import data.dto.AuditLogDto

class AuditLogDataSource(
    private val csvHandler: CsvHandler,
    private val csvParser: AuditLogCsvParser,
) : DataSource<AuditLogDto> {
    override fun fetch(): List<AuditLogDto> {
        val lines = csvHandler.getLines()
        return csvParser.parse(lines)
    }

    override fun save(data: List<AuditLogDto>) {
        val serializedData = csvParser.serialize(data)
        csvHandler.write(serializedData)
    }
}