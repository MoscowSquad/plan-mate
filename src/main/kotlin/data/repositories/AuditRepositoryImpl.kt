package data.repositories

import data.csv_parser.AuditLogCsvParser
import logic.models.AuditLog
import logic.models.AuditType
import logic.repositoies.AuditRepository
import java.io.File
import java.util.*

class AuditRepositoryImpl(private val csvFile: File,
private val csvParser: AuditLogCsvParser) : AuditRepository {

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

    private fun getAllLogs(): List<AuditLog> {
        return csvParser.parse(csvFile.readLines())
    }
}