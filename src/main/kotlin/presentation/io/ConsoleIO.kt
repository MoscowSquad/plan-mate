package presentation.io

import java.util.UUID

interface ConsoleIO {
    fun read(): String
    fun readUUID(): UUID?
    fun write(message: String?)
    fun showError(message: String?)
}

