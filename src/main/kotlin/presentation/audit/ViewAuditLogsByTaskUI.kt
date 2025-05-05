package presentation.audit

import logic.models.AuditLog
import logic.usecases.audit.ViewAuditLogsByTaskUseCase
import presentation.io.ConsoleIO
import java.util.*

class ViewAuditLogsByTaskUI(
    private val viewAuditLogsByTaskUseCase: ViewAuditLogsByTaskUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {

    operator fun invoke() {
        while (true) {
            write(
                """
                
                ✅ TASK AUDIT LOG VIEWER
                
                1️⃣  View logs for specific task
                2️⃣  View all task logs
                3️⃣  Return to Audit Menu
                
                Please choose an option (1-3):
                """.trimIndent()
            )

            when (read().toIntOrNull()) {
                1 -> viewSpecificTaskLogs()
                2 -> viewAllTaskLogs()
                3 -> return
                else -> write("\n❌ Invalid input. Please enter 1, 2, or 3.")
            }
        }
    }

    private fun viewSpecificTaskLogs() {
        try {
            write("\n🔍 ENTER TASK DETAILS")
            val taskId = readUUIDInput("Enter task ID: ")
            val logs = viewAuditLogsByTaskUseCase(taskId)
            displayLogs(logs, "Task ID: $taskId")
        } catch (e: Exception) {
            write("\n❌ Error retrieving task logs: ${e.message}")
        }
    }

    private fun viewAllTaskLogs() {
        try {
            write("\n📋 ALL TASK LOGS")
            val allTasksId = UUID(0, 0)
            val logs = viewAuditLogsByTaskUseCase(allTasksId)
            displayLogs(logs, "All Tasks")
        } catch (e: Exception) {
            write("\n❌ Error retrieving all task logs: ${e.message}")
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