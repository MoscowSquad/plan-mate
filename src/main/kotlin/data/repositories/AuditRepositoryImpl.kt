package data.repositories

import logic.models.AuditLog
import logic.repositoies.AuditRepository
import java.io.File
import java.util.*

class AuditRepositoryImpl(private val csvFile: File) : AuditRepository {

    override fun add(log: AuditLog?): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAllByTaskId(taskId: UUID): List<AuditLog> {
        TODO("Not yet implemented")
    }

    override fun getAllByProjectId(projectId: UUID): List<AuditLog> {
        TODO("Not yet implemented")
    }
}