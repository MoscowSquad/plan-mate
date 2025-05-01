package logic.repositoies

import java.util.UUID

interface ProjectsRepository {
    fun isExist(projectId: UUID): Boolean

}