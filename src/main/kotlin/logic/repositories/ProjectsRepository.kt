package logic.repositories

import logic.models.Project
import java.util.*

interface ProjectsRepository {
    fun addProject(project: Project): Boolean
    fun updateProject(project: Project): Boolean
    fun deleteProject(id: UUID): Boolean
    fun getAllProjects(): List<Project>
    fun getProjectById(id: UUID): Project
}