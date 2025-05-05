package presentation.audit

import presentation.io.ConsoleIO

class AuditUI(
    private val addAuditLogUI: AddAuditLogUI,
    private val viewAuditLogsByProjectUI: ViewAuditLogsByProjectUI,
    private val viewAuditLogsByTaskUI: ViewAuditLogsByTaskUI,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {

    operator fun invoke() {

    }

}