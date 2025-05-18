package presentation.sub_task


import domain.models.Task
import presentation.io.ConsoleIO


class SubTaskUI(
    private val createSubTaskUI: CreateSubTaskUI,
    private val deleteSubTaskUI: DeleteSubTaskUI,
    private val editSubTaskUI: EditSubTaskUI,
    private val getSubTasksByTaskIdUI: GetSubTasksByTaskIdUI,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    suspend operator fun invoke(task: Task) {
        write(
            """
╔════════════════════════════════════════════════════╗
║                   📝 Task Details                  ║
╠════════════════════════════════════════════════════╣
║ 🔹 Title       : ${task.title}
║ 🧾 Description : ${task.description}
║ 🆔 Task ID     : ${task.id}
║ 📌 State ID    : ${task.stateId}
║ 📁 Project ID  : ${task.projectId}
║ 📋 Sub-Tasks   :
${task.subTasks.joinToString(separator = "\n") { "║   • ${it.title} (ID: ${it.id})" }}
╚════════════════════════════════════════════════════╝
""".trimIndent()

        )

        write(
            """
        Select an operation:
        1️⃣ - Create Sub Task
        2️⃣ - Edit Sub Task
        3️⃣ - Delete Sub Task
        4️⃣ - Get Sub Task By Id
        5️⃣ - Back
    """.trimIndent()
        )

        when (read().trim()) {
            "1" -> createSubTaskUI(task)
            "2" -> editSubTaskUI(task)
            "3" -> deleteSubTaskUI(task)
            "4" -> getSubTasksByTaskIdUI()
            "5" -> write("Navigating back...")

            else -> {
                write("❌ Invalid option.")
                invoke(task)
            }
        }
    }
}