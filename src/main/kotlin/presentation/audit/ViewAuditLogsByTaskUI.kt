package presentation.audit

import logic.usecases.audit.ViewAuditLogsByTaskUseCase
import presentation.io.ConsoleIO

class ViewAuditLogsByTaskUI(
    private val viewAuditLogsByTaskUseCase: ViewAuditLogsByTaskUseCase,
    private val consoleIO: ConsoleIO
): ConsoleIO by consoleIO {

}