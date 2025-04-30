package fake

import logic.models.Project
import logic.models.State
import logic.models.Task

fun createTaskHelper(
    taskID: Int? = null,
    taskTitle: String? = null,
    taskDescription: String? = "",
    project: Project? = null,
    state: State? = null
): Task {
    return Task(
        taskID = taskID,
        taskTitle = taskTitle,
        taskDescription = taskDescription,
        project = project,
        state = state,
    )
}