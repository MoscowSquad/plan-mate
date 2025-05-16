package presentation.audit

import presentation.io.ConsoleIO

class AuditUI(
    private val viewAuditLogsByProjectUI: ViewAuditLogsByProjectUI,
    private val viewAuditLogsByTaskUI: ViewAuditLogsByTaskUI,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {

    suspend operator fun invoke() {
        while (true) {
            write(
                """
                🛡️ AUDIT LOG MANAGEMENT SYSTEM
                
                1️⃣  View Audit Logs by Project
                2️⃣  View Audit Logs by Task
                3️⃣  Return to Main Menu
                
                Please choose an option (1-3):
                """.trimIndent()
            )

            when (read().toIntOrNull()) {
                1 -> viewAuditLogsByProjectUI()
                2 -> viewAuditLogsByTaskUI()
                3 -> return
                else -> write("\nInvalid input. Please enter a number between 1 and 3.")
            }
        }
    }
}