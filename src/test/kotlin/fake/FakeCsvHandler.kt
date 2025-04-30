package fake

import utilities.csv_parser.CsvHandler
import utilities.csv_parser.FileGetter

class FakeCsvHandler(fileGetter: FileGetter) : CsvHandler(fileGetter.getFile("test.csv"))