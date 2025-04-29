package utilities.csv_parser

import logic.models.AuditLog

class AuditLogCsvParser(
    private val csvReader: AuditLogCsvReader,
    private val csvWriter: AuditLogCsvWriter,
) : CsvParser<AuditLog> {
    override fun parse(): List<AuditLog> {
        return emptyList()
    }

    override fun serialize(data: List<AuditLog>): CsvData {
        return ""
    }
}