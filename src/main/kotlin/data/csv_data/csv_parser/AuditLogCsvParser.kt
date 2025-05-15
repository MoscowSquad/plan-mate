package data.csv_data.csv_parser

import data.csv_data.dto.AuditLogDto

private const val ID: Int = 0
private const val ACTION: Int = 1
private const val AUDIT_TYPE: Int = 2
private const val TIMESTAMP: Int = 3
private const val ENTITY_ID: Int = 4

class AuditLogCsvParser : CsvParser<AuditLogDto> {
    override fun parse(data: List<CsvData>): List<AuditLogDto> {
        return data.drop(1).map { line ->
            val it = line.split(",")
            AuditLogDto(
                id = it[ID],
                action = it[ACTION],
                auditType = it[AUDIT_TYPE],
                timestamp = it[TIMESTAMP],
                entityId = it[ENTITY_ID],
            )
        }
    }

    override fun serialize(data: List<AuditLogDto>): List<String> {
        return listOf("id,entityType,action,timestamp,entityId,userId") +
                data.map { datum ->
                    "${datum.id},${datum.action},${datum.auditType},${datum.timestamp},${datum.entityId}"
                }
    }
}