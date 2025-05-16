package domain.models

import java.util.*

data class Task(
    val id: UUID,
    val title: String,
    val description: String,
    val projectId: UUID,
    val stateId: UUID,
    val subTasks: List<SubTask>
) {
    fun calculateSubTaskCompletionPercentage(): Double {
        return subTasks.fold(0.0) { acc: Double, subTask: SubTask ->
            acc + if (subTask.isCompleted) 1.0 else 0.0
        }.let {
            if (subTasks.isNotEmpty()) {
                it / subTasks.size * 100
            } else {
                0.0
            }
        }
    }
}
