package logic.usecases.audit

    import io.mockk.every
    import io.mockk.mockk
    import io.mockk.slot
    import io.mockk.verify
    import kotlinx.datetime.LocalDateTime
    import logic.models.AuditLog
    import logic.models.AuditType
    import logic.repositories.AuditRepository
    import org.junit.jupiter.api.Assertions.assertEquals
    import org.junit.jupiter.api.BeforeEach
    import org.junit.jupiter.api.Test
    import java.util.*

    class AddAuditLogUseCaseTest {
        private lateinit var repository: AuditRepository
        private lateinit var useCase: AddAuditLogUseCase

        @BeforeEach
        fun setUp() {
            repository = mockk(relaxed = true)
            useCase = AddAuditLogUseCase(repository)
        }

        @Test
        fun `invoke should call repository addLog with provided log`() {
            // Given
            val log = AuditLog(
                id = UUID.fromString("00000000-0000-0000-0000-000000000020"),
                auditType = AuditType.PROJECT,
                action = "Project created",
                timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
                entityId = UUID.fromString("00000000-0000-0000-0000-000000000020"),
                userId = UUID.fromString("00000000-0000-0000-0000-000000000020")
            )

            // When
            useCase.invoke(log)

            // Then
            verify(exactly = 1) { repository.addLog(log) }
        }

        @Test
        fun `invoke should pass the exact same log object to repository`() {
            // Given
            val log = AuditLog(
                id = UUID.fromString("00000000-0000-0000-0000-000000000020"),
                auditType = AuditType.TASK,
                action = "Task updated",
                timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
                entityId = UUID.fromString("00000000-0000-0000-0000-000000000020"),
                userId = UUID.fromString("00000000-0000-0000-0000-000000000020")
            )
            val logSlot = slot<AuditLog>()
            every { repository.addLog(capture(logSlot)) } returns Unit

            // When
            useCase.invoke(log)

            // Then
            assertEquals(log.id, logSlot.captured.id)
            assertEquals(log.auditType, logSlot.captured.auditType)
            assertEquals(log.action, logSlot.captured.action)
            assertEquals(log.timestamp, logSlot.captured.timestamp)
            assertEquals(log.entityId, logSlot.captured.entityId)
            assertEquals(log.userId, logSlot.captured.userId)
        }

        @Test
        fun `invoke should work with different audit types`() {
            // Given
            val projectLog = AuditLog(
                id = UUID.randomUUID(),
                auditType = AuditType.PROJECT,
                action = "Project deleted",
                timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
                entityId = UUID.randomUUID(),
                userId = UUID.randomUUID()
            )

            val taskLog = AuditLog(
                id = UUID.randomUUID(),
                auditType = AuditType.TASK,
                action = "Task created",
                timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
                entityId = UUID.randomUUID(),
                userId = UUID.randomUUID()
            )

            // When
            useCase.invoke(projectLog)
            useCase.invoke(taskLog)

            // Then
            verify(exactly = 1) { repository.addLog(projectLog) }
            verify(exactly = 1) { repository.addLog(taskLog) }
        }

        @Test
        fun `invoke should handle logs with different timestamps`() {
            // Given
            val oldLog = AuditLog(
                id = UUID.randomUUID(),
                auditType = AuditType.PROJECT,
                action = "Project created",
                timestamp = LocalDateTime(2022, 1, 1, 0, 0, 0),
                entityId = UUID.randomUUID(),
                userId = UUID.randomUUID()
            )

            val newLog = AuditLog(
                id = UUID.randomUUID(),
                auditType = AuditType.PROJECT,
                action = "Project updated",
                timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
                entityId = UUID.randomUUID(),
                userId = UUID.randomUUID()
            )

            // When
            useCase.invoke(oldLog)
            useCase.invoke(newLog)

            // Then
            verify(exactly = 1) { repository.addLog(oldLog) }
            verify(exactly = 1) { repository.addLog(newLog) }
        }
    }