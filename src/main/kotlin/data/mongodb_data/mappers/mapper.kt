package data.mongodb_data.mappers

import data.mongodb_data.dto.*
import data.mongodb_data.util.Constants.ADMIN
import data.mongodb_data.util.Constants.MATE
import data.mongodb_data.util.Constants.PROJECT
import data.mongodb_data.util.Constants.TASK
import domain.models.*
import domain.models.AuditLog.AuditType
import domain.models.User.UserRole
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.*

fun UserDto.toUser(): User {
    return User(
        id = id.toUUID(),
        name = name,
        role = if (role == ADMIN) UserRole.ADMIN else UserRole.MATE,
        projectIds = projectIds.map { it.toUUID() },
        taskIds = taskIds.map { it.toUUID() },
    )
}

fun User.toDto(hashedPassword:String): UserDto {
    return UserDto(
        id = id.toString(),
        name = name,
        hashedPassword = hashedPassword,
        role = if (role == UserRole.ADMIN) ADMIN else MATE,
        projectIds = projectIds.map { it.toString() },
        taskIds = taskIds.map { it.toString() }
    )
}

fun ProjectDto.toProject(): Project {
    return Project(
        id = id.toUUID(),
        name = name,
    )
}

fun Project.toDto(): ProjectDto {
    return ProjectDto(
        id = id.toString(),
        name = name
    )
}

fun TaskDto.toTask(): Task {
    return Task(
        id = UUID.fromString(id),
        title = name,
        description = description,
        projectId = projectId.toUUID(),
        stateId = stateId.toUUID()
    )
}

fun Task.toDto(): TaskDto {
    return TaskDto(
        id = id.toString(),
        name = title,
        description = description,
        projectId = projectId.toString(),
        stateId = stateId.toString()
    )
}

fun TaskStateDto.toTaskState(): TaskState {
    return TaskState(
        id = UUID.fromString(id),
        name = name,
        projectId = projectId.toUUID()
    )
}

fun TaskState.toDto(): TaskStateDto {
    return TaskStateDto(
        id = id.toString(),
        name = name,
        projectId = projectId.toString(),
    )
}

fun AuditLogDto.toAuditLog(): AuditLog {
    return AuditLog(
        id = UUID.fromString(id),
        action = action,
        auditType = if (auditType == PROJECT) AuditType.PROJECT else AuditType.TASK,
        timestamp = timestamp.toTimeStamp(),
        entityId = entityId.toUUID(),
    )
}

fun AuditLog.toDto(): AuditLogDto {
    return AuditLogDto(
        id = id.toString(),
        action = action,
        auditType = if (auditType == AuditType.PROJECT) PROJECT else TASK,
        timestamp = timestamp.toString(),
        entityId = entityId.toString(),
    )
}

fun isValidUUIDStrict(uuidString: String): Boolean {
    val uuidRegex = Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    return uuidRegex.matches(uuidString)
}

fun String.toUUID(): UUID {
    return if (isValidUUIDStrict(this)) UUID.fromString(this)
    else throw IllegalArgumentException("UUID $this isn't valid")
}

fun String.toTimeStamp(): LocalDateTime {
    val instant = Instant.fromEpochMilliseconds(this.toLong())
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return localDateTime
}
