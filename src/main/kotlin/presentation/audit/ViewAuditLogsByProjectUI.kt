package presentation.audit

import logic.usecases.audit.ViewAuditLogsByProjectUseCase
import presentation.io.ConsoleIO

class ViewAuditLogsByProjectUI(
    private val viewAuditLogsByProjectUseCase: ViewAuditLogsByProjectUseCase,
    private val consoleIO: ConsoleIO
): ConsoleIO by consoleIO {

}