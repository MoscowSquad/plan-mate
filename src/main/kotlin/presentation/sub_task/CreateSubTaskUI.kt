package presentation.sub_task

import domain.usecases.sub_task.CreateSubTaskUseCase

class CreateSubTaskUI(
    private val createSubTaskUseCase: CreateSubTaskUseCase
) {/*
    suspend operator fun invoke() {
        write("Please enter task ID:")
        val taskId = readUUID()
        write("Please enter task name:")
        val subTaskName = readUUID()
        write("Please enter task ID:")
        val taskId = readUUID()


        if (taskId == null) {
            write("❌ Invalid task ID.")
            return
        }

        runCatching { deleteTaskUseCase(taskId) }
            .onSuccess { write("✅ Task deleted successfully.") }
            .onFailure { write("❌ Failed to delete task: ${it.message}") }

    }
    */
}