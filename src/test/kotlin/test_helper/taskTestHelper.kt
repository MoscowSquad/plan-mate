package test_helper

import data.csv_data.dto.TaskDto


fun getTasksCsvLines(): List<String> {
    return listOf(
        "id,title,description,projectId,stateId",
        "6d3c0dfc-5b05-479d-be86-4d31b22582cc,Video 101 to 120,,6d3c0dfc-5b05-479d-be86-4d31b22582cc,6d3c0dfc-5b05-479d-be86-4d31b22582cc",
        "6d3c0dfc-5b05-479d-be86-4d31b22582cc,Video 201 to 220,Don't watch video N0.203,6d3c0dfc-5b05-479d-be86-4d31b22582cc,6d3c0dfc-5b05-479d-be86-4d31b22582cc",
        "6d3c0dfc-5b05-479d-be86-4d31b22582cc,Video 301 to 320,,6d3c0dfc-5b05-479d-be86-4d31b22582cc,6d3c0dfc-5b05-479d-be86-4d31b22582cc",
    )
}

fun createTask(
    id: String,
    title: String,
    description: String,
    projectId: String,
    stateId: String,
): TaskDto = TaskDto(id, title, description, projectId, stateId)