package data.mongodb_data.datasource

import data.csv_data.csv_parser.CsvHandler
import data.csv_data.csv_parser.ProjectCsvParser

class ProjectDataSource(
    private val csvHandler: CsvHandler,
    private val csvParser: ProjectCsvParser,
) {
}