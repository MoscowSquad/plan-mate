package presentation.audit

import logic.models.AuditLog
import logic.usecases.audit.ViewAuditLogsByProjectUseCase
import presentation.io.ConsoleIO
import java.util.*

class ViewAuditLogsByProjectUI(
    private val viewAuditLogsByProjectUseCase: ViewAuditLogsByProjectUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {

    operator fun invoke() {
        while (true) {
            write(
                """
                
                🏗️ VIEW AUDIT LOGS BY PROJECT
                
                1️⃣  View logs for specific project
                2️⃣  View all project logs
                3️⃣  Return to Audit Menu
                
                Please choose an option (1-3):
                """.trimIndent()
            )

            when (read().toIntOrNull()) {
                1 -> viewSpecificProjectLogs()
                2 -> viewAllProjectLogs()
                3 -> return
                else -> write("\n❌ Invalid input. Please enter 1, 2, or 3.")
            }
        }
    }

    private fun viewSpecificProjectLogs() {
        try {
            write("\n🔍 ENTER PROJECT DETAILS")
            val projectId = readUUIDInput("Enter project ID: ")
            val logs = viewAuditLogsByProjectUseCase(projectId)
            displayLogs(logs, "Project ID: $projectId")
        } catch (e: Exception) {
            write("\n❌ Error retrieving project logs: ${e.message}")
        }
    }

    private fun viewAllProjectLogs() {
        try {
            write("\n📋 ALL PROJECT LOGS")
            val allProjectsId = UUID(0, 0)
            val logs = viewAuditLogsByProjectUseCase(allProjectsId)
            displayLogs(logs, "All Projects")
        } catch (e: Exception) {
            write("\n❌ Error retrieving all project logs: ${e.message}")
        }
    }

    private fun displayLogs(logs: List<AuditLog>, context: String) {
        if (logs.isEmpty()) {
            write("\nℹ️ No audit logs found for $context")
            return
        }

        write("\n📝 AUDIT LOGS ($context) - ${logs.size} entries")
        logs.forEachIndexed { index, log ->
            write(
                """
                ${index + 1}. [${log.timestamp}] ${log.action} | ${log.auditType}
                   👤 User: ${log.userId}
                   📌 Entity: ${log.entityId}
                """.trimIndent()
            )
        }
    }

    private fun readUUIDInput(prompt: String): UUID {
        while (true) {
            write(prompt)
            try {
                return UUID.fromString(read().trim())
            } catch (e: IllegalArgumentException) {
                write("❌ Invalid UUID format. Please try again.")
            }
        }
    }
}