package presentation.sub_task

import domain.models.SubTask
import domain.usecases.sub_task.UpdateSubTaskUseCase
import presentation.io.ConsoleIO
import java.util.UUID

class EditSubTaskUI (
    private val updateSubTaskUseCase: UpdateSubTaskUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {

}