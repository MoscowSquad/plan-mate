package presentation.audit

import presentation.io.ConsoleIO
import java.util.*

class AuditUI(
    private val addAuditLogUI: AddAuditLogUI,
    private val viewAuditLogsByProjectUI: ViewAuditLogsByProjectUI,
    private val viewAuditLogsByTaskUI: ViewAuditLogsByTaskUI,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {

    operator fun invoke() {
        while (true) {
            write(
                """
                
                🛡️ AUDIT LOG MANAGEMENT SYSTEM
                
                1️⃣  Add New Audit Log Entry
                2️⃣  View Audit Logs by Project
                3️⃣  View Audit Logs by Task
                4️⃣  Return to Main Menu
                
                Please choose an option (1-4):
                """.trimIndent()
            )

            when (read().toIntOrNull()) {
                1 -> addAuditLogUI()
                2 -> viewAuditLogsByProjectUI
                3 -> viewAuditLogsByTaskUI
                4 -> return
                else -> write("\nInvalid input. Please enter a number between 1 and 4.")
            }
        }
    }
}