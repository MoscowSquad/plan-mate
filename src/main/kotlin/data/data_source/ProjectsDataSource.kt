package data.data_source

import data.mongodb_data.dto.ProjectDto
import java.util.UUID

interface ProjectsDataSource {
    suspend fun addProject(project: ProjectDto): Boolean
    suspend fun updateProject(project: ProjectDto): Boolean
    suspend fun deleteProject(id: UUID): Boolean
    suspend fun getAllProjects(): List<ProjectDto>
    suspend fun getProjectById(id: UUID): ProjectDto
}