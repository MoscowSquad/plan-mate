package data.mongodb_data.repositories

import data.mongodb_data.datasource.StateDataSource
import data.mongodb_data.mappers.toDto
import data.mongodb_data.mappers.toTask
import data.mongodb_data.mappers.toTaskState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import logic.models.TaskState
import logic.repositories.StateRepository
import java.util.*

class StateRepositoryImpl(
    private val stateDataSource: StateDataSource
):StateRepository {
    private val scope = CoroutineScope(Dispatchers.IO)
    override fun getById(id: UUID): TaskState {
        val deferred = scope.async {
            stateDataSource.getById(id).toTaskState()
        }
        return deferred.getCompleted()
    }

    override fun getByProjectId(projectId: UUID): List<TaskState> {
        val deferred = scope.async {
            stateDataSource.getByProjectId(projectId).map{
                it.toTaskState()
            }
        }
        return deferred.getCompleted()
    }

    override fun update(state: TaskState): Boolean {
        try {
            scope.launch {
                stateDataSource.update(state.toDto())
            }
            return true
        }catch (e : Exception){
            return false
        }
    }

    override fun add(projectId: UUID, state: TaskState): Boolean {
       try {
           scope.launch {
               stateDataSource.add(projectId,state.toDto())
           }
           return true
       }catch (e : Exception){
           return false
       }
    }

    override fun delete(projectId: UUID, stateId: UUID): Boolean {
        try {
            scope.launch {
                stateDataSource.delete(projectId,stateId)
            }
            return true
        }catch (e : Exception){
            return false
        }
    }
}