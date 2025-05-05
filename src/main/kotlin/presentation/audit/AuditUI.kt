package presentation.audit

import logic.usecases.audit.ViewAuditLogsByProjectUseCase
import logic.usecases.audit.ViewAuditLogsByTaskUseCase
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