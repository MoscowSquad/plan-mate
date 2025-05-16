package presentation.sub_task

import domain.usecases.sub_task.CreateSubTaskUseCase

class CreateSubTaskUI(
    private val createSubTaskUseCase: CreateSubTaskUseCase
) {
    suspend operator fun invoke() {

    }
}