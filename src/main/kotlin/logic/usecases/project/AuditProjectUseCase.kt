package logic.usecases.project

import logic.models.AuditLog
import logic.repositoies.AuditRepository
import logic.repositoies.project.AuditProjectRepository
import logic.repositoies.project.ProjectsRepository
import logic.repositoies.project.exception.ProjectException.ProjectNotFoundException
import logic.repositoies.project.exception.validateProjectExists
import java.util.*

class AuditProjectUseCase (
    private val auditRepository: AuditProjectRepository,
    ) {

        fun getSpecificAuditByProject(projectID: UUID,auditId:UUID):AuditLog{
          validateProjectExists(projectID)
            return auditRepository.getSpecificAuditByProject(projectID,auditId)
        }

      fun getAllAuditByProject(projectID: UUID):List<AuditLog>{
        validateProjectExists(projectID)
        return auditRepository.getAllAuditByProject(projectID)
     }
    fun addAuditToProject(projectID: UUID, auditId: AuditLog){
        validateProjectExists(projectID)
        auditRepository.addAuditToProject(projectID,auditId)
    }
}