package presentation.audit

import logic.usecases.audit.AddAuditLogUseCase
import presentation.io.ConsoleIO

class AddAuditLogUI (
    private val addAuditLogUseCase: AddAuditLogUseCase,
    private val consoleIO: ConsoleIO
): ConsoleIO by consoleIO {

}