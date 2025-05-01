package logic.repositoies

import logic.models.Project
import logic.models.State
import java.util.UUID

interface ProjectsRepository {
    fun isExist(projectId: UUID): Boolean
    fun addState(state: State): Boolean
    fun getAll(): List<Project>?
    fun getById(projectId: UUID): Project?

}
