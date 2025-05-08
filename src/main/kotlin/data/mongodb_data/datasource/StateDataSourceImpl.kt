package data.mongodb_data.datasource

import logic.models.TaskState
import java.util.*

class StateDataSourceImpl():StateDataSource {
    override suspend fun getById(id: UUID): TaskState {
        TODO("Not yet implemented")
    }

    override suspend fun getByProjectId(projectId: UUID): List<TaskState> {
        TODO("Not yet implemented")
    }

    override suspend fun update(state: TaskState): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun add(projectId: UUID, state: TaskState): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun delete(projectId: UUID, stateId: UUID): Boolean {
        TODO("Not yet implemented")
    }


}