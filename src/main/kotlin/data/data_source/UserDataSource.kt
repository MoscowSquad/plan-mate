package data.data_source

import data.csv_data.dto.UserDto
import java.util.*

interface UserDataSource {
    suspend fun register(user: UserDto): UserDto
    suspend fun login(name: String, password: String): UserDto
    suspend fun addUser(user: UserDto): Boolean
    suspend fun deleteUser(id: UUID):Boolean
    suspend fun assignUserToProject(projectId: UUID, userId: UUID): Boolean
    suspend fun unassignUserFromProject(projectId: UUID, userId: UUID): Boolean
    suspend fun getUserById(id: UUID): UserDto
    suspend fun getAllUsers(): List<UserDto>
}