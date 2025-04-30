package logic.repositoies

import java.util.UUID

interface ProjectsRepository {
    fun isProjectExist(projectId: UUID): Boolean

}