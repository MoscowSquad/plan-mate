package utilities.csv_parser

import java.io.File

abstract class FileGetter() {
    abstract fun getFile(fileName: String): File
}