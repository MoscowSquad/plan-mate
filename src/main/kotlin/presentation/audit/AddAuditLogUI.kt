package presentation.audit

import kotlinx.datetime.Clock
import kotlinx.datetime.toLocalDateTime
import logic.models.AuditLog
import logic.models.AuditType
import logic.usecases.audit.AddAuditLogUseCase
import presentation.io.ConsoleIO
import java.util.*
import kotlinx.datetime.TimeZone

class AddAuditLogUI(
    private val addAuditLogUseCase: AddAuditLogUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {

    operator fun invoke() {
        while (true) {
            write(
                """
                
                📝 AUDIT LOG MANAGEMENT
                
                1️⃣  Create New Audit Log Entry
                2️⃣  Return to Main Menu
                
                Please choose an option (1-2):
                """.trimIndent()
            )

            when (val input = read()) {
                "1" -> createNewAuditLog()
                "2" -> return
                else -> write("\nInvalid input. Please enter 1 or 2.")
            }
        }
    }

    private fun createNewAuditLog() {
        write("\n➕ CREATE NEW AUDIT LOG ENTRY")

        try {
            val userId = readUUIDInput("Enter user ID: ")
            val entityId = readUUIDInput("Enter entity ID: ")
            val action = readInput("Enter action (e.g., CREATE, UPDATE, DELETE): ")
            val auditType = readAuditTypeInput()

            val auditLog = AuditLog(
                id = UUID.randomUUID(),
                action = action,
                auditType = auditType,
                timestamp = Clock.System.now().toLocalDateTime(TimeZone.UTC),
                entityId = entityId,
                userId = userId
            )

            addAuditLogUseCase.invoke(auditLog)
            addAuditLogUseCase.invoke(log = auditLog)
            write("\n✅ Audit log created successfully!")
        } catch (e: Exception) {
            write("\n❌ Error creating audit log: ${e.message}")
        }
    }

    private fun readUUIDInput(prompt: String): UUID {
        while (true) {
            write(prompt)
            try {
                return UUID.fromString(read().trim())
            } catch (e: IllegalArgumentException) {
                write("Invalid UUID format. Please try again.")
            }
        }
    }

    private fun readAuditTypeInput(): AuditType {
        while (true) {
            write(
                """
                Select audit type:
                1. TASK
                2. PROJECT
                Enter choice (1-2):
                """.trimIndent()
            )

            when (read().toIntOrNull()) {
                1 -> return AuditType.TASK
                2 -> return AuditType.PROJECT
                else -> write("Invalid selection. Please try again.")
            }
        }
    }

    private fun readInput(prompt: String, optional: Boolean = false): String {
        while (true) {
            write(prompt)
            val input = read().trim()
            if (input.isNotBlank() || optional) {
                return input
            }
            write("This field is required. Please try again.")
        }
    }
}