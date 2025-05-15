package data.mappers

import data.csv_data.dto.*
import data.csv_data.mappers.*
import data.csv_data.util.ADMIN
import data.csv_data.util.MATE
import data.csv_data.util.PROJECT
import data.csv_data.util.TASK
import kotlinx.datetime.LocalDateTime
import logic.models.*
import logic.models.AuditLog.AuditType
import logic.models.User.UserRole
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class MapperKtTest {

    @Test
    fun `test UserDto to User conversion`() {
        val userDto = UserDto(
            id = "550e8400-e29b-41d4-a716-446655440000",
            name = "Test User",
            hashedPassword = "hashed123",
            role = ADMIN,
            projectIds = listOf("550e8400-e29b-41d4-a716-446655440001"),
            taskIds = emptyList()
        )

        val user = userDto.toUser()

        assertEquals(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), user.id)
        assertEquals("Test User", user.name)
        assertEquals(UserRole.ADMIN, user.role)
        assertEquals(1, user.projectIds.size)
        assertEquals(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"), user.projectIds[0])
    }

    @Test
    fun `test User to UserDto conversion`() {
        val hashedPassword = "hashed123"
        val user = User(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            name = "Test User",
            role = UserRole.MATE,
            projectIds = listOf(UUID.fromString("550e8400-e29b-41d4-a716-446655440001")),
            taskIds = emptyList()
        )

        val userDto = user.toDto(hashedPassword)

        assertEquals("550e8400-e29b-41d4-a716-446655440000", userDto.id)
        assertEquals("Test User", userDto.name)
        assertEquals("hashed123", userDto.hashedPassword)
        assertEquals(MATE, userDto.role)
        assertEquals(1, userDto.projectIds.size)
        assertEquals("550e8400-e29b-41d4-a716-446655440001", userDto.projectIds[0])
    }

    @Test
    fun `test ProjectDto to Project conversion`() {
        val projectDto = ProjectDto(
            id = "550e8400-e29b-41d4-a716-446655440000",
            name = "Test Project"
        )

        val project = projectDto.toProject()

        assertEquals(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), project.id)
        assertEquals("Test Project", project.name)
    }

    @Test
    fun `test Project to ProjectDto conversion`() {
        val project = Project(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            name = "Test Project"
        )

        val projectDto = project.toDto()

        assertEquals("550e8400-e29b-41d4-a716-446655440000", projectDto.id)
        assertEquals("Test Project", projectDto.name)
    }

    @Test
    fun `test TaskDto to Task conversion`() {
        val taskDto = TaskDto(
            id = "550e8400-e29b-41d4-a716-446655440000",
            name = "Test Task",
            description = "Task Description",
            projectId = "550e8400-e29b-41d4-a716-446655440001",
            stateId = "550e8400-e29b-41d4-a716-446655440002"
        )

        val task = taskDto.toTask()

        assertEquals(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), task.id)
        assertEquals("Test Task", task.title)
        assertEquals("Task Description", task.description)
        assertEquals(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"), task.projectId)
        assertEquals(UUID.fromString("550e8400-e29b-41d4-a716-446655440002"), task.stateId)
    }

    @Test
    fun `test Task to TaskDto conversion`() {
        val task = Task(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            title = "Test Task",
            description = "Task Description",
            projectId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001"),
            stateId = UUID.fromString("550e8400-e29b-41d4-a716-446655440002")
        )

        val taskDto = task.toDto()

        assertEquals("550e8400-e29b-41d4-a716-446655440000", taskDto.id)
        assertEquals("Test Task", taskDto.name)
        assertEquals("Task Description", taskDto.description)
        assertEquals("550e8400-e29b-41d4-a716-446655440001", taskDto.projectId)
        assertEquals("550e8400-e29b-41d4-a716-446655440002", taskDto.stateId)
    }

    @Test
    fun `test TaskStateDto to TaskState conversion`() {
        val taskStateDto = TaskStateDto(
            id = "550e8400-e29b-41d4-a716-446655440000",
            name = "To Do",
            projectId = "550e8400-e29b-41d4-a716-446655440001"
        )

        val taskState = taskStateDto.toTaskState()

        assertEquals(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), taskState.id)
        assertEquals("To Do", taskState.name)
        assertEquals(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"), taskState.projectId)
    }

    @Test
    fun `test TaskState to TaskStateDto conversion`() {
        val taskState = TaskState(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            name = "To Do",
            projectId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001")
        )

        val taskStateDto = taskState.toDto()

        assertEquals("550e8400-e29b-41d4-a716-446655440000", taskStateDto.id)
        assertEquals("To Do", taskStateDto.name)
        assertEquals("550e8400-e29b-41d4-a716-446655440001", taskStateDto.projectId)
    }

    @Test
    fun `test AuditLogDto to AuditLog conversion`() {
        val timestamp = "2023-01-01T12:00:00"
        val auditLogDto = AuditLogDto(
            id = "550e8400-e29b-41d4-a716-446655440000",
            action = "Created",
            auditType = PROJECT,
            timestamp = timestamp,
            entityId = "550e8400-e29b-41d4-a716-446655440001"
        )

        val auditLog = auditLogDto.toAudiLog()

        assertEquals(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), auditLog.id)
        assertEquals("Created", auditLog.action)
        assertEquals(AuditType.PROJECT, auditLog.auditType)
        assertEquals(LocalDateTime.parse(timestamp), auditLog.timestamp)
        assertEquals(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"), auditLog.entityId)
    }

    @Test
    fun `test AuditLog to AuditLogDto conversion`() {
        val timestamp = LocalDateTime.parse("2023-01-01T12:00:00")
        val auditLog = AuditLog(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            action = "Created",
            auditType = AuditType.TASK,
            timestamp = timestamp,
            entityId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001")
        )

        val auditLogDto = auditLog.toDto()

        assertEquals("550e8400-e29b-41d4-a716-446655440000", auditLogDto.id)
        assertEquals("Created", auditLogDto.action)
        assertEquals(TASK, auditLogDto.auditType)
        assertEquals(timestamp.toString(), auditLogDto.timestamp)
        assertEquals("550e8400-e29b-41d4-a716-446655440001", auditLogDto.entityId)
    }

    @Test
    fun `test String to UUID conversion`() {
        val uuidString = "550e8400-e29b-41d4-a716-446655440000"

        val uuid = uuidString.toUUID()

        assertEquals(UUID.fromString(uuidString), uuid)
    }

    @Test
    fun `test String to LocalDateTime conversion`() {
        val dateTimeString = "2023-01-01T12:00:00"

        val dateTime = dateTimeString.toTimeStamp()

        assertEquals(LocalDateTime.parse(dateTimeString), dateTime)
    }

    @Test
    fun `test UserDto to User role conversion with different roles`() {
        val adminDto = UserDto(
            id = "550e8400-e29b-41d4-a716-446655440000",
            name = "Admin User",
            hashedPassword = "hashed123",
            role = ADMIN,
            projectIds = listOf(),
            taskIds = listOf()
        )
        assertEquals(UserRole.ADMIN, adminDto.toUser().role)

        // Test MATE conversion
        val mateDto = UserDto(
            id = "550e8400-e29b-41d4-a716-446655440000",
            name = "Mate User",
            hashedPassword = "hashed123",
            role = MATE,
            projectIds = listOf(),
            taskIds = listOf()
        )
        assertEquals(UserRole.MATE, mateDto.toUser().role)

        val unknownRoleDto = UserDto(
            id = "550e8400-e29b-41d4-a716-446655440000",
            name = "Unknown Role User",
            hashedPassword = "hashed123",
            role = "UNKNOWN_ROLE",
            projectIds = listOf(),
            taskIds = listOf()
        )
        assertEquals(UserRole.MATE, unknownRoleDto.toUser().role)
    }

    @Test
    fun `test User to UserDto role conversion with different roles`() {
        val hashedPassword = "hashed123"
        val adminUser = User(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            name = "Admin User",
            role = UserRole.ADMIN,
            projectIds = listOf(),
            taskIds = listOf()
        )
        assertEquals(ADMIN, adminUser.toDto(hashedPassword).role)

        val mateUser = User(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            name = "Mate User",
            role = UserRole.MATE,
            projectIds = listOf(),
            taskIds = listOf()
        )
        assertEquals(MATE, mateUser.toDto(hashedPassword).role)
    }

    @Test
    fun `test AuditLogDto to AuditLog auditType conversion with different types`() {
        val timestamp = "2023-01-01T12:00:00"

        val projectAuditLogDto = AuditLogDto(
            id = "550e8400-e29b-41d4-a716-446655440000",
            action = "Created",
            auditType = PROJECT,
            timestamp = timestamp,
            entityId = "550e8400-e29b-41d4-a716-446655440001"
        )
        assertEquals(AuditType.PROJECT, projectAuditLogDto.toAudiLog().auditType)

        val taskAuditLogDto = AuditLogDto(
            id = "550e8400-e29b-41d4-a716-446655440000",
            action = "Created",
            auditType = TASK,
            timestamp = timestamp,
            entityId = "550e8400-e29b-41d4-a716-446655440001"
        )
        assertEquals(AuditType.TASK, taskAuditLogDto.toAudiLog().auditType)
    }

    @Test
    fun `test AuditLog to AuditLogDto auditType conversion with different types`() {
        val timestamp = LocalDateTime.parse("2023-01-01T12:00:00")

        val projectAuditLog = AuditLog(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            action = "Created",
            auditType = AuditType.PROJECT,
            timestamp = timestamp,
            entityId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001")
        )
        assertEquals(PROJECT, projectAuditLog.toDto().auditType)

        val taskAuditLog = AuditLog(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            action = "Created",
            auditType = AuditType.TASK,
            timestamp = timestamp,
            entityId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001")
        )
        assertEquals(TASK, taskAuditLog.toDto().auditType)
    }
}