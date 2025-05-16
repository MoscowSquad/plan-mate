package presentation.audit

import data.mongodb_data.mappers.toUUID
import domain.models.AuditLog
import domain.usecases.audit.ViewAuditLogsByTaskUseCase
import presentation.io.ConsoleIO
import java.util.*

class ViewAuditLogsByTaskUI(
    private val viewAuditLogsByTaskUseCase: ViewAuditLogsByTaskUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {

    suspend operator fun invoke() {
        while (true) {
            try {
                val taskId = readUUIDInput("Enter task ID (or type 'exit' to quit): ") ?: return

                val logs = viewAuditLogsByTaskUseCase(taskId)
                displayLogs(logs, "Task ID: $taskId")
            } catch (e: Exception) {
                write("\n❌ Error retrieving task logs: ${e.message}")
            }
        }
    }

    private fun displayLogs(logs: List<AuditLog>, context: String) {
        if (logs.isEmpty()) {
            write("\nℹ️ No audit logs found for $context")
            return
        }

        write("\n📝 AUDIT LOGS ($context) - ${logs.size} entries")
        write("=========================================")
        logs.forEachIndexed { index, log ->
            write(
                """
                ${index + 1}. [${log.timestamp}] ${log.action} | ${log.auditType}
                   📌 Entity: ${log.entityId}
                """.trimIndent()
            )
        }
        write("=========================================")
    }

    private fun readUUIDInput(prompt: String): UUID? {
        while (true) {
            write(prompt)
            val input = read().trim()
            if (input.equals("exit", ignoreCase = true)) return null
            try {
                return input.toUUID()
            } catch (e: IllegalArgumentException) {
                write("❌ Invalid UUID format. Please try again.")
            }
        }
    }
}