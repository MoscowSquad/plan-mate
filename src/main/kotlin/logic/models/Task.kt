package logic.models


data class Task(
    val id: Int? = null,
    var title: String? = null,
    var description: String? = "",
    val project: Project?=null,
    var state: State? = null,
)
