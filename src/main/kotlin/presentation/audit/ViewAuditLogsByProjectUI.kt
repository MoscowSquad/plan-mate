package presentation.audit

import data.mongodb_data.mappers.toUUID
import domain.models.AuditLog
import domain.usecases.audit.ViewAuditLogsByProjectUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import presentation.io.ConsoleIO
import java.util.*

class ViewAuditLogsByProjectUI(
    private val viewAuditLogsByProjectUseCase: ViewAuditLogsByProjectUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    private val scope = CoroutineScope(Dispatchers.IO)
    suspend operator fun invoke() {
        while (true) {
            try {
                val projectId = readUUIDInput("Enter project ID (or type 'exit' to quit): ") ?: return

                val logs = scope.async { viewAuditLogsByProjectUseCase(projectId) }
                displayLogs(logs.await(), "Project ID: $projectId")
            } catch (e: Exception) {
                write("\n❌ Error retrieving project logs: ${e.message}")
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