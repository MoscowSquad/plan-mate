package logic.repositoies

import java.util.UUID

interface ProjectsRepository {
    fun isExist(projectId: UUID): Boolean
    fun addState(state: String): Boolean

}