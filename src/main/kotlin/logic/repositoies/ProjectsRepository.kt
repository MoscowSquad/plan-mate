package logic.repositoies

import logic.models.State
import java.util.UUID
import logic.models.Project

interface ProjectsRepository {
    fun isExist(projectId: UUID): Boolean
    fun addState(state: State): Boolean

    fun save(project: Project): Project
    fun findById(id: UUID): Project?
    fun delete(id: UUID): Boolean
    fun assignUserToProject(projectId: UUID, userId: UUID): Boolean
}