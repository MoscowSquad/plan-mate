package presentation.sub_task

import domain.usecases.sub_task.GetSubTaskPrcentageUseCase
import presentation.io.ConsoleIO


class CalculateSubTaskPercentageUI(
    private val getSubTaskPercentageUseCase: GetSubTaskPrcentageUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {

    suspend operator fun invoke() {
        /*
        write("Please enter Task ID:")
        val taskId = readUUID()

        if (taskId == null) {
            write("❌ Invalid Task ID.")
            return
        }
*/
        runCatching { getSubTaskPercentageUseCase() }
            .onSuccess { percentage ->
                write("✅ Sub-task completion percentage: %.2f%%".format(percentage))
            }
            .onFailure {
                write("❌ Failed to calculate percentage: ${it.message}")
            }
    }
}
