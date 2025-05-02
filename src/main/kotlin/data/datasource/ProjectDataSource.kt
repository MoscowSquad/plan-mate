package data.datasource

import logic.models.Project
import data.csv_parser.CsvHandler
import data.csv_parser.ProjectCsvParser

class ProjectDataSource(
    private val csvHandler: CsvHandler,
    private val csvParser: ProjectCsvParser,
) : DataSource<Project> {
    override fun fetch(): List<Project> {
        TODO("Not yet implemented")
    }

    override fun save(data: List<Project>) {
        TODO("Not yet implemented")
    }
}