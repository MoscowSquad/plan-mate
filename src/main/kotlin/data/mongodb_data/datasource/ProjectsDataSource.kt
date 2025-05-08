package data.mongodb_data.datasource

import logic.models.Project
import java.util.*

interface ProjectsDataSource {
    suspend fun addProject(project: Project): Boolean
    suspend fun updateProject(project: Project): Boolean
    suspend fun deleteProject(id: UUID): Boolean
    suspend fun getAllProjects(): List<Project>
    suspend fun getProjectById(id: UUID): Project
}



