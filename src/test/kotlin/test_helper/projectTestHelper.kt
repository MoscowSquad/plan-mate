package test_helper

import data.csv_data.dto.ProjectDto

fun getProjects(): List<ProjectDto> {
    return listOf(
        createProject("045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f", "The chance"),
        createProject("045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f", "Work"),
        createProject("045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f", "Homework"),
    )
}

fun createProject(id: String, name: String): ProjectDto {
    return ProjectDto(id, name)
}

fun getProjectCsvLines(): List<String> {
    return listOf(
        "id,name",
        "045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f,The chance",
        "045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f,Work",
        "045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f,Homework",
    )
}