package utilities.csv_parser

import java.io.File

class SourceFileGetter : FileGetter() {
    override fun getFile(fileName: String): File {
        return File("")
    }
}