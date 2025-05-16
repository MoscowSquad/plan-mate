package domain.repositories

import domain.models.Project
import java.util.*

interface ProjectsRepository {
    suspend fun addProject(project: Project): Boolean
    suspend fun updateProject(project: Project): Boolean
    suspend fun deleteProject(id: UUID): Boolean
    suspend fun getAllProjectsByUser(userId: UUID): List<Project>
    suspend fun getProjectById(id: UUID): Project
}