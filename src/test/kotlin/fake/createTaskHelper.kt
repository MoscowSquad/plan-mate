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
        id = taskID,
        title = taskTitle,
        description = taskDescription,
        project = project,
        state = state,
    )
}