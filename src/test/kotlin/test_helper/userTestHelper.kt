package test_helper

import data.csv_data.dto.UserDto
import data.csv_data.util.ADMIN
import data.csv_data.util.MATE
import data.session_manager.LoggedInUser
import logic.models.User
import java.util.*

fun getUsers(): List<UserDto> {
    return listOf(
        createUser("82e16049-a9fb-4f69-b6f7-3336b68f2ae4", "Aiman", "3336b68f2ae4", ADMIN, emptyList()),
        createUser(
            "045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f",
            "Zeyad",
            "09sd23od3skd",
            ADMIN,
            listOf("82e16049-a9fb-4f69-b6f7-3336b68f2ae4")
        ),
        createUser(
            "07f641d4-077e-4f08-978d-3b6c9587f4bf",
            "Yaser",
            "wk5dr98sd6dd",
            MATE,
            listOf("82e16049-a9fb-4f69-b6f7-3336b68f2ae4", "82e16049-a9fb-4f69-b6f7-3336b68f2ae4")
        ),
    )
}

fun getUsersCsvLines(): List<String> {
    return listOf(
        "id,name,hashedPassword,role,projectIds",
        "82e16049-a9fb-4f69-b6f7-3336b68f2ae4,Aiman,3336b68f2ae4,$ADMIN,[]",
        "045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f,Zeyad,09sd23od3skd,$ADMIN,[82e16049-a9fb-4f69-b6f7-3336b68f2ae4]",
        "07f641d4-077e-4f08-978d-3b6c9587f4bf,Yaser,wk5dr98sd6dd,$MATE,[82e16049-a9fb-4f69-b6f7-3336b68f2ae4, 82e16049-a9fb-4f69-b6f7-3336b68f2ae4]",
    )
}

fun createUser(
    id: String,
    userName: String,
    hashedPassword: String,
    role: String,
    projectIds: List<String>
): UserDto {
    return UserDto(
        id, userName, hashedPassword, role, projectIds,
        taskIds = listOf()
    )
}


fun createLoginUser(userUUID: UUID, projectIds: List<UUID> = emptyList()): LoggedInUser {
    return LoggedInUser(userUUID, "", User.UserRole.ADMIN, projectIds = projectIds)
}