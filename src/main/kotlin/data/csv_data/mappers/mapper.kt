package data.csv_data.mappers

import data.csv_data.dto.*
import data.csv_data.util.ADMIN
import data.csv_data.util.MATE
import data.csv_data.util.PROJECT
import data.csv_data.util.TASK
import kotlinx.datetime.LocalDateTime
import logic.models.*
import logic.models.AuditLog.AuditType
import logic.models.User.UserRole
import java.util.*

fun UserDto.toUser(): User {
    return User(
        id = id.toUUID(),
        name = name,
        hashedPassword = hashedPassword,
        role = if (role == ADMIN) UserRole.ADMIN else UserRole.MATE,
        projectIds = projectIds.map { it.toUUID() }
    )
}

fun User.toDto(): UserDto {
    return UserDto(
        id = id.toString(),
        name = name,
        hashedPassword = hashedPassword,
        role = if (role == UserRole.ADMIN) ADMIN else MATE,
        projectIds = projectIds.map { it.toString() }
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

fun AuditLogDto.toAudiLog(): AuditLog {
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


fun String.toUUID(): UUID {
    return UUID.fromString(this)
}

fun String.toTimeStamp(): LocalDateTime {
    return LocalDateTime.parse(this)
}
