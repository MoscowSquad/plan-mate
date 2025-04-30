package logic.models


data class Task(
    val taskID: Int? = null,
    var taskTitle: String? = null,
    var taskDescription: String? = "",
    val project: Project?=null,
    var state: State? = null,
)
