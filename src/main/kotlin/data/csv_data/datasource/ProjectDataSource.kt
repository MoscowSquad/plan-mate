package data.csv_data.datasource

import data.csv_data.csv_parser.CsvHandler
import data.csv_data.csv_parser.ProjectCsvParser
import data.csv_data.dto.ProjectDto

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