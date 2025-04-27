package presentation.io

import java.util.*

class ConsoleIOImpl(
    private val scanner: Scanner
) : ConsoleIO {
    override fun read(): String {
        return scanner.nextLine()
    }

    override fun write(message: String?) {
        println(message)
    }

    override fun showError(message: String?) {
        val red = "\u001B[31m"    // Red for failure
        val reset = "\u001B[0m"   // Reset color

        println("${red}$message${reset}")
    }
}