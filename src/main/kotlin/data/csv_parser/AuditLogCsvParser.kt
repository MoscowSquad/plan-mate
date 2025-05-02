package data.csv_parser

import logic.models.AuditLog

class AuditLogCsvParser : CsvParser<AuditLog> {
    override fun parse(data: List<CsvData>): List<AuditLog> {
        TODO("Not yet implemented")
    }

    override fun serialize(data: List<AuditLog>): List<String> {
        TODO("Not yet implemented")
    }
}