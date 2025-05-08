package data.mongodb_data.datasource

import logic.models.TaskState
import java.util.*

class TaskStateDataSourceImpl():TaskStateDataSource {
    override suspend fun getTaskStateById(id: UUID): TaskState {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskStateByProjectId(projectId: UUID): List<TaskState> {
        TODO("Not yet implemented")
    }

    override suspend fun updateTaskState(state: TaskState): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun addTaskState(projectId: UUID, state: TaskState): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTaskState(projectId: UUID, stateId: UUID): Boolean {
        TODO("Not yet implemented")
    }

}