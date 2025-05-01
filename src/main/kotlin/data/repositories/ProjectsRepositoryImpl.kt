package data.repositories

import logic.models.Project
import logic.repositoies.ProjectsRepository
import java.util.*

class ProjectsRepositoryImpl : ProjectsRepository {
    private val projects = mutableMapOf<UUID, Project>()
    private val projectUserAssignments = mutableMapOf<UUID, MutableSet<UUID>>()

    override fun save(project: Project): Project {
        projects[project.id] = project
        return project
    }

    override fun findById(id: UUID): Project? {
        return projects[id]
    }

    override fun delete(id: UUID): Boolean {
        return if (projects.containsKey(id)) {
            projects.remove(id)
            projectUserAssignments.remove(id)
            true
        } else {
            false
        }
    }

    override fun assignUserToProject(projectId: UUID, userId: UUID): Boolean {
        if (!projects.containsKey(projectId)) {
            return false
        }

        val userSet = projectUserAssignments.getOrPut(projectId) { mutableSetOf() }
        return userSet.add(userId)
    }
}