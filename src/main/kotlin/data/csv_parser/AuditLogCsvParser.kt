package data.csv_parser

import logic.models.AuditLog
import logic.models.AuditType
import utilities.AuditLogIndex

class AuditLogCsvParser : CsvParser<AuditLog> {
    override fun parse(data: List<CsvData>): List<AuditLog> {
        return data.drop(1).map { line ->
            val it = line.split(",")
            AuditLog(
                id = it[AuditLogIndex.ID].toUUID(),
                action = it[AuditLogIndex.ACTION],
                auditType = it[AuditLogIndex.AUDIT_TYPE].toAuditType(),
                timestamp = it[AuditLogIndex.TIMESTAMP].toTimeStamp(),
                entityId = it[AuditLogIndex.ENTITY_ID].toUUID(),
                userId = it[AuditLogIndex.USER_ID].toUUID(),
            )
        }
    }

    private fun String.toAuditType(): AuditType {
        return when (this) {
            AuditType.PROJECT.toString() -> AuditType.PROJECT
            AuditType.TASK.toString() -> AuditType.TASK
            else -> throw Exception("No audit-type named $this")
        }
    }

    override fun serialize(data: List<AuditLog>): List<String> {
        return listOf("id,entityType,action,timestamp,entityId,userId") +
                data.map { datum ->
                    "${datum.id},${datum.action},${datum.auditType},${datum.timestamp},${datum.entityId},${datum.userId}"
                }
    }
}