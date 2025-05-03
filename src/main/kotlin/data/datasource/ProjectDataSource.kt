package data.datasource

import data.dto.ProjectDto
import data.csv_parser.CsvHandler
import data.csv_parser.ProjectCsvParser

class ProjectDataSource(
    private val csvHandler: CsvHandler,
    private val csvParser: ProjectCsvParser,
) : DataSource<ProjectDto> {
    override fun fetch(): List<ProjectDto> {
        val lines = csvHandler.getLines()
        return csvParser.parse(lines)
    }

    override fun save(data: List<ProjectDto>) {
        val serializedData = csvParser.serialize(data)
        csvHandler.write(serializedData)
    }
}