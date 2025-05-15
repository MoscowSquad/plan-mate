package test_helper

import data.csv_data.dto.TaskStateDto
fun getTaskStateCsvLines(): List<String> {
    return listOf(
        "id,title,projectId",
        "caf22bb1-90ff-409d-bef2-3b8bc9759354,Todo,caf22bb1-90ff-409d-bef2-3b8bc9759354",
        "caf22bb1-90ff-409d-bef2-3b8bc9759354,In Progress,caf22bb1-90ff-409d-bef2-3b8bc9759354",
        "caf22bb1-90ff-409d-bef2-3b8bc9759354,Done,caf22bb1-90ff-409d-bef2-3b8bc9759354",
    )
}

fun createTaskState(
    id: String,
    title: String,
    projectId: String,
): TaskStateDto {
    return TaskStateDto(id, title, projectId)
}