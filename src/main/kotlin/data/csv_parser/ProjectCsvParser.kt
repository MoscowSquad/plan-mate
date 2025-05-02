package data.csv_parser

import logic.models.Project

class ProjectCsvParser : CsvParser<Project> {
    override fun parse(data: List<CsvData>): List<Project> {
        TODO("Not yet implemented")
    }

    override fun serialize(data: List<Project>): List<String> {
        TODO("Not yet implemented")
    }
}