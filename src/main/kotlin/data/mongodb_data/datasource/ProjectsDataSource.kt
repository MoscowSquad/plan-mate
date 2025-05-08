package data.mongodb_data.datasource

import logic.models.Project
import java.util.*

interface ProjectsDataSource {
    suspend fun addProject(project: Project)
    suspend fun updateProject(project: Project)
    suspend fun deleteProject(id: UUID)
    suspend fun getAllProjects(): List<Project>
    suspend fun getProjectById(id: UUID): Project
}