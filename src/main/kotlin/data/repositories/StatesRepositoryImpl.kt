package data.repositories

import logic.models.TaskState
import logic.repositoies.StatesRepository
import java.util.*

class StatesRepositoryImpl: StatesRepository {
    override fun deleteState(projectId: UUID, stateId: UUID): Boolean {
        return false
    }

    override fun getStateById(stateId: UUID): TaskState {
        return TaskState(UUID.randomUUID(), " ", UUID.randomUUID())
    }
}