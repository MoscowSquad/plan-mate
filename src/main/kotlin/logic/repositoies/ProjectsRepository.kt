package logic.repositoies

import logic.models.Project
import java.util.UUID

interface ProjectsRepository {
    fun add(project: Project): Boolean
    fun update(project: Project): Boolean
    fun delete(id: UUID): Boolean
    fun getAll(): List<Project>

}