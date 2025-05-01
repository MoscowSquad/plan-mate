package logic.usecases

import logic.models.AuditLog
import logic.repositoies.AuditProjectRepository
import utilities.exception.ProjectException

import java.util.*

class AuditProjectUseCase (
    private val auditRepository: AuditProjectRepository,
    private val projectExistenceValidator: ProjectExistenceValidator
    ) {

        fun getSpecificAuditByProject(projectID: UUID,auditId:UUID):AuditLog{
            projectExistenceValidator.isExist(projectID)
            return auditRepository.getByProjectId(projectID,auditId)
                ?: throw ProjectException.UserNotFoundException()
        }

      fun getAllAuditByProject(projectID: UUID):List<AuditLog>{
          projectExistenceValidator.isExist(projectID)
          return auditRepository.getAll(projectID)
              ?: throw ProjectException.UserNotFoundException()
       }

      fun addAuditToProject(projectID: UUID, auditId: AuditLog){
        projectExistenceValidator.isExist(projectID)
          return auditRepository.addAuditToProject(projectID,auditId)

       }

}