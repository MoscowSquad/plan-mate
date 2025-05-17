package domain.usecases.sub_task

import domain.models.Task

class GetSubTaskPrcentageUseCase(private val subTask: Task) {
    operator fun invoke(): Double {
        return subTask.calculateSubTaskCompletionPercentage()
    }
}
