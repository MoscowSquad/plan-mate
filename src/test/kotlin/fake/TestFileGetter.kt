package fake

import utilities.csv_parser.FileGetter
import java.io.File

class TestFileGetter : FileGetter() {
    override fun getFile(fileName: String): File {
        return File(requireNotNull(javaClass.classLoader.getResource(fileName)).toURI())
    }
}