package fake

import presentation.io.ConsoleIO
import java.util.*

open class FakeConsoleIO(
    private val inputs: Queue<String>,
    val outputs: MutableList<String?> = mutableListOf()
) : ConsoleIO {
    override fun read(): String = inputs.poll()
    override fun readUUID(): UUID? {
        return UUID.randomUUID()
    }

    override fun write(message: String?) {
        outputs.add(message)
    }

    override fun showError(message: String?) {
        outputs.add(message)
    }
}
