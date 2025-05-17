package presentation.sub_task

import domain.usecases.sub_task.GetSubTasksByTaskIdUseCase
import presentation.io.ConsoleIO

class GetSubTasksByTaskIdUI (
    private val getSubTasksByTaskIdUseCase: GetSubTasksByTaskIdUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {

}