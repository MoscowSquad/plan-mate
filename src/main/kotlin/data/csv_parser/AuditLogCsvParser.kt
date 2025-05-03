package data.csv_parser

import data.dto.AuditLogDto
import data.util.AuditLogIndex

class AuditLogCsvParser : CsvParser<AuditLogDto> {
    override fun parse(data: List<CsvData>): List<AuditLogDto> {
        return data.drop(1).map { line ->
            val it = line.split(",")
            AuditLogDto(
                id = it[AuditLogIndex.ID],
                action = it[AuditLogIndex.ACTION],
                auditType = it[AuditLogIndex.AUDIT_TYPE],
                timestamp = it[AuditLogIndex.TIMESTAMP],
                entityId = it[AuditLogIndex.ENTITY_ID],
                userId = it[AuditLogIndex.USER_ID],
            )
        }
    }

    override fun serialize(data: List<AuditLogDto>): List<String> {
        return listOf("id,entityType,action,timestamp,entityId,userId") +
                data.map { datum ->
                    "${datum.id},${datum.action},${datum.auditType},${datum.timestamp},${datum.entityId},${datum.userId}"
                }
    }
}