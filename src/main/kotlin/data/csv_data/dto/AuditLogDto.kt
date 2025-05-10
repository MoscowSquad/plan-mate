package data.csv_data.dto

data class AuditLogDto(
    val id: String,
    val action: String,
    val auditType: String,
    val timestamp: String,
    val entityId: String,
)


