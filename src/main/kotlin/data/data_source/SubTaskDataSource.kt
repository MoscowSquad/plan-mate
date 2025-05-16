package data.data_source

import data.mongodb_data.dto.SubTaskDto

interface SubTaskDataSource {
    suspend fun getSubTasksByTaskId(taskId: String): List<SubTaskDto>
    suspend fun addSubTask(subTask: SubTaskDto): Boolean
    suspend fun updateSubTask(subTask: SubTaskDto): Boolean
    suspend fun deleteSubTask(subTaskId: String): Boolean
}