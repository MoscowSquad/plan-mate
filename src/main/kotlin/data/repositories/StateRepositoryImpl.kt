package data.repositories

import logic.models.TaskState
import logic.repositoies.ProjectsRepository
import logic.repositoies.StateRepository
import java.util.*

class StateRepositoryImpl(
    private val projectsRepository: ProjectsRepository
) : StateRepository {

    override fun getById(id: UUID): TaskState {
        return TaskState(UUID.randomUUID(), "", UUID.randomUUID()) // fake to TDD
    }

    override fun getByProjectId(projectId: UUID): List<TaskState> {
        return emptyList()
    }

    override fun update(state: TaskState): Boolean {
        return false // fake to TDD
    }

    override fun add(projectId: UUID, title: String): Boolean {
        return false // fake to TDD
    }

    override fun delete(projectId: UUID, stateId: UUID): Boolean {
        return false
    }

}