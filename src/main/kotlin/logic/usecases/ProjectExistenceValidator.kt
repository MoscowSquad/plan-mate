package logic.usecases

import logic.repositoies.ProjectsRepository
import utilities.exception.ProjectException.ProjectNotFoundException
import java.util.*

class ProjectExistenceValidator(private val projectsRepository: ProjectsRepository) {

     fun isExist(projectId: UUID): Boolean {
         if (projectsRepository.getAll() ==null)
            throw ProjectNotFoundException(projectId.toString())
       return true

}}
