package data.mongodb_data.repositories

import data.mongodb_data.datasource.TaskStateDataSource
import data.mongodb_data.mappers.toDto
import data.mongodb_data.mappers.toTaskState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import logic.models.TaskState
import logic.repositories.TaskStateRepository
import java.util.*

class TaskStateRepositoryImpl(
    private val taskStateDataSource: TaskStateDataSource
):TaskStateRepository {
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun getTaskStateById(id: UUID): TaskState {
        val deferred = scope.async {
            taskStateDataSource.getTaskStateById(id).toTaskState()
        }
        return deferred.getCompleted()
    }

    override fun getTaskStateByProjectId(projectId: UUID): List<TaskState> {
        val deferred = scope.async {
            taskStateDataSource.getTaskStateByProjectId(projectId).map{
                it.toTaskState()
            }
        }
        return deferred.getCompleted()
    }

    override fun updateTaskState(state: TaskState): Boolean {
        try {
            scope.launch {
                taskStateDataSource.updateTaskState(state.toDto())
            }
            return true
        }catch (e : Exception){
            return false
        }
    }

    override fun addTaskState(projectId: UUID, state: TaskState): Boolean {
        try {
            scope.launch {
                taskStateDataSource.addTaskState(projectId,state.toDto())
            }
            return true
        }catch (e : Exception){
            return false
        }
    }

    override fun deleteTaskState(projectId: UUID, stateId: UUID): Boolean {
        try {
            scope.launch {
                taskStateDataSource.deleteTaskState(projectId,stateId)
            }
            return true
        }catch (e : Exception){
            return false
        }
    }
}