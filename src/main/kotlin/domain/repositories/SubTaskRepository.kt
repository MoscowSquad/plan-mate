package domain.repositories

import domain.models.SubTask
import java.util.*

interface SubTaskRepository {
    suspend fun getSubTasksByTaskId(taskId: UUID): List<SubTask>
    suspend fun createSubTask(subTask: SubTask): Boolean
    suspend fun updateSubTask(subTask: SubTask): Boolean
    suspend fun deleteSubTask(subTaskId: UUID): Boolean
}