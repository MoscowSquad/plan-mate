package data.mongodb_data.datasource

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.data_source.SubTaskDataSource
import data.mongodb_data.dto.SubTaskDto
import kotlinx.coroutines.flow.toList

class SubTaskDataSourceImpl(
    private val collection: MongoCollection<SubTaskDto>
) : SubTaskDataSource {

    override suspend fun getSubTasksByTaskId(taskId: String): List<SubTaskDto> {
        return collection.find(
            Filters.eq(
                SubTaskDto::parentTaskId.name, taskId
            )
        ).toList()
    }

    override suspend fun addSubTask(subTask: SubTaskDto): Boolean {
        return collection.insertOne(subTask).wasAcknowledged()
    }

    override suspend fun updateSubTask(subTask: SubTaskDto): Boolean {
        return collection.updateOne(
            Filters.eq(SubTaskDto::id.name, subTask.id),
            Updates.combine(
                Updates.set(SubTaskDto::id.name, subTask.id),
                Updates.set(SubTaskDto::title.name, subTask.title),
                Updates.set(SubTaskDto::description.name, subTask.description),
                Updates.set(SubTaskDto::isCompleted.name, subTask.isCompleted),
                Updates.set(SubTaskDto::parentTaskId.name, subTask.parentTaskId)
            )
        ).wasAcknowledged()
    }

    override suspend fun deleteSubTask(subTaskId: String): Boolean {
        return collection.deleteOne(
            Filters.eq(SubTaskDto::id.name, subTaskId)
        ).wasAcknowledged()
    }
}