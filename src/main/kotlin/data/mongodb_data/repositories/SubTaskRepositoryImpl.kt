package data.mongodb_data.repositories

import data.data_source.SubTaskDataSource
import data.mongodb_data.mappers.toDto
import data.mongodb_data.mappers.toSubTask
import domain.models.SubTask
import domain.repositories.SubTaskRepository
import java.util.*

class SubTaskRepositoryImpl(
    private val subTaskDataSource: SubTaskDataSource,
) : SubTaskRepository {
    override suspend fun getSubTasksByTaskId(taskId: UUID): List<SubTask> {
        return subTaskDataSource.getSubTasksByTaskId(taskId.toString()).map {
            it.toSubTask()
        }
    }

    override suspend fun createSubTask(subTask: SubTask): Boolean {
        return subTaskDataSource.addSubTask(subTask.toDto())
    }

    override suspend fun updateSubTask(subTask: SubTask): Boolean {
        return subTaskDataSource.updateSubTask(subTask.toDto())
    }

    override suspend fun deleteSubTask(subTaskId: UUID): Boolean {
        return subTaskDataSource.deleteSubTask(subTaskId.toString())
    }
}