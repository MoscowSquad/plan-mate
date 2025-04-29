package utilities.csv_parser

import logic.models.Project

class ProjectCsvParser(
    private val csvReader: ProjectCsvReader,
    private val csvWriter: ProjectCsvWriter,
) : CsvParser<Project> {
    override fun parse(): List<Project> {
        return emptyList()
    }

    override fun serialize(data: List<Project>): CsvData {
        return ""
    }
}