package data.csv_data.dto

data class UserDto(
    val id: String,
    val name: String,
    val hashedPassword: String,
    val role: String,
    val projectIds: List<String>,
    val taskIds: List<String>
)