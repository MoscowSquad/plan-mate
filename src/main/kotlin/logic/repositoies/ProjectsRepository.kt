package logic.repositoies

import java.util.*


interface ProjectsRepository {
    fun isExist(projectId: UUID): Boolean

}