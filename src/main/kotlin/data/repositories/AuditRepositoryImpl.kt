package data.repositories

import kotlinx.datetime.LocalDateTime
import logic.models.AuditLog
import logic.models.AuditType
import logic.repositoies.AuditRepository
import java.io.File
import java.util.*

class AuditRepositoryImpl(private val csvFile: File) : AuditRepository {

    init {
        if (!csvFile.exists()) {
            csvFile.createNewFile()
            csvFile.appendText("id,auditType,action,timestamp,entityId,userId\n")
        }
    }

    override fun add(log: AuditLog?): Boolean {
        try {
            csvFile.appendText(
                "${log?.id},${log?.auditType},${log?.action}," +
                        "${log?.timestamp},${log?.entityId},${log?.userId}\n"
            )
            return true
        } catch (e: Exception){
            return false
        }
    }

    override fun getAllByTaskId(taskId: UUID): List<AuditLog> {
        return getAllLogs().filter { it.auditType == AuditType.TASK && it.entityId == taskId }    }

    override fun getAllByProjectId(projectId: UUID): List<AuditLog> {
        return getAllLogs().filter { it.auditType == AuditType.PROJECT && it.entityId == projectId }
    }

    //
    private fun getAllLogs(): List<AuditLog> {
        return csvFile.readLines()
            .drop(1)
            .mapNotNull { line ->
                val parts = line.split(',')
                if (parts.size == 6) {
                    try {
                        AuditLog(
                            id = UUID.fromString(parts[0]),
                            auditType = AuditType.valueOf(parts[1]),
                            action = parts[2],
                            timestamp = LocalDateTime.parse(parts[3]),
                            entityId = UUID.fromString(parts[4]),
                            userId = UUID.fromString(parts[5])
                        )
                    } catch (e: Exception) {
                        null
                    }
                } else {
                    null
                }
            }
    }
}