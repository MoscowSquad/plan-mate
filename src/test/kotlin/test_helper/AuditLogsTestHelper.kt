package test_helper

import data.csv_data.dto.AuditLogDto
import data.csv_data.util.PROJECT
import data.csv_data.util.TASK


fun getSerializedCsvLines(timestamp: String): List<String> {
    return listOf(
        "id,entityType,action,timestamp,entityId,userId",
        "12a6e381-379d-492e-a4a1-f367733f449d,Add,$PROJECT,$timestamp,12a6e381-379d-492e-a4a1-f367733f449d",
        "12a6e381-379d-492e-a4a1-f367733f449d,Edit,$PROJECT,$timestamp,12a6e381-379d-492e-a4a1-f367733f449d",
        "12a6e381-379d-492e-a4a1-f367733f449d,Add,$TASK,$timestamp,12a6e381-379d-492e-a4a1-f367733f449d",
        "12a6e381-379d-492e-a4a1-f367733f449d,Edit,$TASK,$timestamp,12a6e381-379d-492e-a4a1-f367733f449d",
        "12a6e381-379d-492e-a4a1-f367733f449d,Delete,$PROJECT,$timestamp,12a6e381-379d-492e-a4a1-f367733f449d"
    )
}

fun createAuditLog(
    id: String,
    action: String,
    auditType: String,
    timestamp: String,
    entityId: String,
): AuditLogDto {
    return AuditLogDto(id, action, auditType, timestamp, entityId)
}

fun getAuditLogs(timestamp: String): List<AuditLogDto> {
    return listOf(
        createAuditLog(
            "12a6e381-379d-492e-a4a1-f367733f449d", "Add", PROJECT, timestamp,
            "12a6e381-379d-492e-a4a1-f367733f449d"
        ),
        createAuditLog(
            "12a6e381-379d-492e-a4a1-f367733f449d", "Edit", PROJECT, timestamp,
            "12a6e381-379d-492e-a4a1-f367733f449d"
        ),
        createAuditLog(
            "12a6e381-379d-492e-a4a1-f367733f449d", "Add", TASK, timestamp,
            "12a6e381-379d-492e-a4a1-f367733f449d"
        ),
        createAuditLog(
            "12a6e381-379d-492e-a4a1-f367733f449d", "Edit", TASK, timestamp,
            "12a6e381-379d-492e-a4a1-f367733f449d"
        ),
        createAuditLog(
            "12a6e381-379d-492e-a4a1-f367733f449d", "Delete", PROJECT, timestamp,
            "12a6e381-379d-492e-a4a1-f367733f449d"
        ),
    )
}

fun getCsvLines(timestamp: String): List<String> {
    return listOf(
        "id,entityType,action,timestamp,entityId,userId",
        "12a6e381-379d-492e-a4a1-f367733f449d,Add,$PROJECT,$timestamp,12a6e381-379d-492e-a4a1-f367733f449d,12a6e381-379d-492e-a4a1-f367733f449d",
        "12a6e381-379d-492e-a4a1-f367733f449d,Edit,$PROJECT,$timestamp,12a6e381-379d-492e-a4a1-f367733f449d,12a6e381-379d-492e-a4a1-f367733f449d",
        "12a6e381-379d-492e-a4a1-f367733f449d,Add,$TASK,$timestamp,12a6e381-379d-492e-a4a1-f367733f449d,12a6e381-379d-492e-a4a1-f367733f449d",
        "12a6e381-379d-492e-a4a1-f367733f449d,Edit,$TASK,$timestamp,12a6e381-379d-492e-a4a1-f367733f449d,12a6e381-379d-492e-a4a1-f367733f449d",
        "12a6e381-379d-492e-a4a1-f367733f449d,Delete,$PROJECT,$timestamp,12a6e381-379d-492e-a4a1-f367733f449d,12a6e381-379d-492e-a4a1-f367733f449d",
    )
}