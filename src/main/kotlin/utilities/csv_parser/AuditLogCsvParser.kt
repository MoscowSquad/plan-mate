package utilities.csv_parser

import logic.models.AuditLog

class AuditLogCsvParser : CsvParser<AuditLog> {
    override fun parse(): List<AuditLog> {
        return emptyList()
    }

    override fun serialize(data: List<AuditLog>): CsvData {
        return ""
    }
}