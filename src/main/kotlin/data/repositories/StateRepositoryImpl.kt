package data.repositories

import data.datasource.TaskStateDataSource
import logic.models.TaskState
import logic.repositories.StateRepository
import java.util.*

class StateRepositoryImpl(
    private val stateDataSource: TaskStateDataSource
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