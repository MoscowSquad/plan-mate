package data.repositories

import logic.models.TaskState
import logic.repositoies.ProjectsRepository
import java.util.*

class ProjectsRepositoryImpl: ProjectsRepository {
    override fun isExist(projectId: UUID): Boolean {
        return false
    }

    override fun deleteState(projectId: UUID, stateId: UUID): Boolean {
        return false
    }

    override fun getStateById(stateId: UUID): TaskState {
        return TaskState(UUID.randomUUID(), "State", UUID.randomUUID())
    }


}